/**
 * This class is used to represent an object of query evaluator of this program.
 * <p>
 * Note that this query evaluator supports multiple (arbitary) query terms with
 * expressions NOT, AND, OR.
 *
 * @author Antreas Dionysiou
 * @since 30/9/2018
 */

import java.util.*;

public class Boolean_Module {

    Indexing_Module index_module = new Indexing_Module();
    TreeMap<String, Record> index = null;

    /**
     * Constructor 1.
     */
    public Boolean_Module() {
        //initialize
    }

    /**
     * This method is used as the main search method that is called when user enters a query to search
     * in a specific collection that is given.
     *
     * @return void
     */
    public void search() {

        System.out.println();
        System.out.println("Please give the name of the collection to search from:");
        Scanner scan = null;
        String collection_name = "";
        TreeMap<String, Record> index = null;
        do {
            scan = new Scanner(System.in);
            collection_name = scan.nextLine();
            StringTokenizer strtok = new StringTokenizer(collection_name, " ");
            if (strtok.countTokens() > 1) {
                System.out.println("Please give one term as the collection name! Please give the collection's name again!");
                continue;
            }
            //check if directory exists
            else if (!index_module.check_directory(collection_name)) {
                //directory does not exist.
                continue;
            } else {
                //directory exists so deserialize (if exists) or create the treemap.
                index = this.index_module.find_serealized_treemap(collection_name);
                if (index == null) {
                    //serialized treemap does not exist so create it.
                    System.out.println();
                    System.out.println("Serialized index didn't exist so we are creating it now. Please wait.");
                    index = this.index_module.create_index(collection_name);
                    //serialize the new index just in case...
                    index_module.serialize_treemap(collection_name, index, 1);
                    System.out.println();
                    System.out.println("Index created and serialized in collection's directory.");
                }
                //till here index has been deserialized or created successfully.
                //break the loop
                break;
            }
        } while (true);
        //till here index has been deserialized or created successfully.

        //ask for the logical expression
        System.out.println();
        String query = "";
        scan = new Scanner(System.in);
        System.out.println("Please enter the query that you want to search for: (with spaces separating each term)");
        query = scan.nextLine();
        //start timer to see the speed of searching.
        long start = System.nanoTime();
        StringTokenizer strtok = new StringTokenizer(query, " ");

        //create an arraylist and fill it with the query tokens.
        ArrayList<String> tokens = new ArrayList<String>();
        while (strtok.hasMoreTokens()) {
            //add token to list.

            //first make it lowercase
            String token = strtok.nextToken().toLowerCase();

            //stemm token
            Stemmer stemmer = new Stemmer();
            char[] tok1 = token.toCharArray();
            stemmer.add(tok1, tok1.length);
            stemmer.stem();
            //System.out.println("Stemmed token: "+stemmer.toString());
            token = stemmer.toString();

            //and finally add it to tokens arraylist.
            tokens.add(token);
        }

        //fill the tokens_records arraylist
        ArrayList<Query_term> tokens_records = new ArrayList<Query_term>();
        for (String term : tokens) {
            tokens_records.add(new Query_term(term, index));
        }
        //evaluate the term records.

        //1st evaluate NOT terms
        //get treemap values (Record objects).
        Collection<Record> records = index.values();
        ArrayList<Posting> all_docids = new ArrayList<Posting>();
        for (Record rec : records) {
            all_docids.addAll(rec.posting_list);
        }

        Set<Integer> p1 = new HashSet<Integer>();
        TreeMap<Integer, String> no_duplicates = new TreeMap<Integer, String>();

        for (Posting p : all_docids) {
            if (!p1.add(p.doc_id)) {
                //duplicate record so skip it.
                continue;
            } else {
                //insert it to Arraylist. The record has already added to hashset on p1.add line.
                no_duplicates.put(p.doc_id, p.description);
            }
        }
        //treemap with all doc ids and description created with no duplicates (no_duplicates).

        //evaluate NOTs.
        for (int i = 0; i < tokens_records.size(); i++) {
            if (tokens_records.get(i).term.equals("not")) {

                //check NOT's syntax
                try {
                    if (tokens_records.get(i + 1) == null) {
                        //exception will thrown if any of 2 is null
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("WRONG QUERY SYNTAX!!! (NOT)");
                    //for now exit
                    System.exit(0);
                }

                //get postings for the not term to not be included.
                if (tokens_records.get(i + 1).record != null) {
                    //get postings of term in query not 'term' so to NOT inlcude them
                    ArrayList<Posting> not_postings = index.get(tokens_records.get(i + 1).term).posting_list;
                    //create a new arraylist answpost for the solution
                    Query_term answer_to_not = new Query_term();
                    Record answ_rec = new Record();
                    ArrayList<Posting> answ_post = new ArrayList<Posting>();

                    //iterate on all no_duplicates treemap
                    Set<Integer> docids = no_duplicates.keySet();
                    int dont_include_flag = 0;
                    for (Integer docid : docids) {
                        dont_include_flag = 0;
                        //check if it is contained in not postings.
                        for (Posting post : not_postings) {
                            if (docid == post.doc_id) {
                                //docid shouldnt be included.
                                dont_include_flag = 1;
                                break;
                            }
                        }
                        if (dont_include_flag == 1) {
                            //skip docid.
                            continue;
                        } else {
                            //the docid should be included, so include it.
                            answ_post.add(new Posting(docid, no_duplicates.get(docid), 0));
                        }
                    }
                    //not evaluated and answered so connect objects.

                    //if not a posting found that satisfies not term then set record to null
                    if (answ_post.size() == 0) {
                        answer_to_not.term = "ANS";
                        answer_to_not.record = null;

                    } else if (answ_post.size() > 0) {
                        //first put answer posting list to answer record
                        answ_rec.posting_list = answ_post;
                        //then add answer record to query term object. term="ANS" that means resolution of other expression
                        answer_to_not.term = "ANS";
                        answer_to_not.record = answ_rec;
                    }
                    //now place Query_term obj back to postion in Query term array list and remove the two terms.
                    tokens_records.add(i, answer_to_not);
                    //remove not term
                    tokens_records.remove(i + 1);
                    //remove the next term of not term.
                    tokens_records.remove(i + 1);
                    //now the position i in tokens_record has the answer to not term.
                } else if (tokens_records.get(i + 1).record == null) {
                    //term does not exist in index so include all postings.
                    Query_term answer_to_not = new Query_term();
                    Record answ_rec = new Record();

                    //put all no_dupliacte postings to answ_rec posting list.
                    //iterate on all no_duplicates treemap
                    Set<Integer> docids1 = no_duplicates.keySet();
                    for (Integer docid : docids1) {
                        answ_rec.posting_list.add(new Posting(docid, no_duplicates.get(docid), 0));
                    }

                    //answ_rec.posting_list=null;
                    answer_to_not.term = "ANS";
                    answer_to_not.record = answ_rec;
                    tokens_records.add(i, answer_to_not);
                    tokens_records.remove(i + 1);
                    tokens_records.remove(i + 1);

                }

            }
        }
        //NOTS evaluated successfuly

        //2nd evaluate ANDS
        for (int i = 0; i < tokens_records.size(); i++) {
            if (tokens_records.get(i).term.equals("and")) {
                //create solution objects.
                Query_term answ = new Query_term();
                answ.term = "ANS";
                Record answ_rec = new Record();
                ArrayList<Posting> answ_postings = new ArrayList<Posting>();

                //check AND's syntax.
                try {
                    if (tokens_records.get(i + 1) == null || tokens_records.get(i - 1) == null) {
                        //exception will thrown if any of 2 is null
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("WRONG QUERY SYNTAX!!! (AND)");
                    //for now exit
                    System.exit(0);
                }

                if (tokens_records.get(i - 1).record != null && tokens_records.get(i + 1).record != null) {

                    //iterate on left's term postings
                    ArrayList<Posting> left = tokens_records.get(i - 1).record.posting_list;
                    ArrayList<Posting> right = tokens_records.get(i + 1).record.posting_list;
                    if (left == null || right == null) {
                        System.out.println("WRONG SYNTAX!!! (AND)");
                        //for now exit
                        System.exit(0);
                    }

                    int left_size = left.size();
                    int right_size = right.size();
                    int left_pointer = 0;
                    int right_pointer = 0;
                    while (left_size > 0 && right_size > 0) {
                        //not the end of one of the two lists so check if the docids are the same to get them.
                        if (left.get(left_pointer).doc_id == right.get(right_pointer).doc_id) {
                            //same docid found so include it in the solution!.
                            answ_postings.add(new Posting(left.get(left_pointer).doc_id, left.get(left_pointer).description, 0));
                            left_pointer++;
                            right_pointer++;
                            left_size--;
                            right_size--;
                        }
                        //else see which is the smaller to move pointer.
                        else if (left.get(left_pointer).doc_id < right.get(right_pointer).doc_id) {
                            //left docid is smaller so move the left's pointer one position.
                            left_pointer++;
                            left_size--;
                        } else if (left.get(left_pointer).doc_id > right.get(right_pointer).doc_id) {
                            //right docid is smaller so move the right's pointer one position.
                            right_pointer++;
                            right_size--;
                        }

                    }

                    //answ_postings has the solution.
                    //bind the objcts together.
                    answ_rec.posting_list = answ_postings;
                    answ.term = "ANS";
                    answ.record = answ_rec;

                    //finally place answer ANS to right position and remove the appropriate ones.
                    tokens_records.add(i - 1, answ);
                    tokens_records.remove(i);
                    tokens_records.remove(i);
                    tokens_records.remove(i);
                } else if (tokens_records.get(i - 1).record == null || tokens_records.get(i + 1).record == null) {
                    answ.term = "ANS";
                    answ.record = null;
                    tokens_records.add(i - 1, answ);
                    tokens_records.remove(i);
                    tokens_records.remove(i);
                    tokens_records.remove(i);

                }
                //set lookup for ands's at the beginning.
                i = 0;
            }
        }

        //3rd and final evaluate ORs
        for (int i = 0; i < tokens_records.size(); i++) {
            if (tokens_records.get(i).term.equals("or")) {
                //create solution objects.
                Query_term answ = new Query_term();
                answ.term = "ANS";
                Record answ_rec = new Record();
                ArrayList<Posting> answ_postings = new ArrayList<Posting>();

                //check OR syntax
                try {
                    if (tokens_records.get(i + 1) == null || tokens_records.get(i - 1) == null) {
                        //exception will thrown if any of 2 is null
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("WRONG QUERY SYNTAX!!! (OR)");
                    //for now exit
                    System.exit(0);
                }

                if (tokens_records.get(i - 1).record != null && tokens_records.get(i + 1).record != null) {

                    ArrayList<Posting> left = tokens_records.get(i - 1).record.posting_list;
                    ArrayList<Posting> right = tokens_records.get(i + 1).record.posting_list;

                    if (left == null || right == null) {
                        System.out.println("WRONG SYNTAX!!! (OR)");
                        //for now exit
                        System.exit(0);
                    }

                    int left_size = left.size();
                    int right_size = right.size();
                    int left_pointer = 0;
                    int right_pointer = 0;
                    while (left_size > 0 && right_size > 0) {
                        //not the end of one of the two lists so check if the docids are the same to get them.
                        if (left.get(left_pointer).doc_id == right.get(right_pointer).doc_id) {
                            //same docid found so include it in the solution!.
                            answ_postings.add(new Posting(left.get(left_pointer).doc_id, left.get(left_pointer).description, 0));
                            left_pointer++;
                            right_pointer++;
                            left_size--;
                            right_size--;
                        }
                        //else see which is the smaller to move pointer.
                        else if (left.get(left_pointer).doc_id < right.get(right_pointer).doc_id) {
                            //left docid is smaller so move the left's pointer one position and add smaller (left) to list.
                            answ_postings.add(new Posting(left.get(left_pointer).doc_id, left.get(left_pointer).description, 0));
                            left_pointer++;
                            left_size--;
                        } else if (left.get(left_pointer).doc_id > right.get(right_pointer).doc_id) {
                            //right docid is smaller so move the right's pointer one position and add smaller (right) to list.
                            answ_postings.add(new Posting(right.get(right_pointer).doc_id, right.get(right_pointer).description, 0));
                            right_pointer++;
                            right_size--;
                        }
                    }
                    //add remaining left's or right's postings.
                    //any while of the ones below will be activated according to which list has still postings.
                    while (left_size > 0) {
                        answ_postings.add(new Posting(left.get(left_pointer).doc_id, left.get(left_pointer).description, 0));
                        left_size--;
                        left_pointer++;
                    }
                    while (right_size > 0) {
                        answ_postings.add(new Posting(right.get(right_pointer).doc_id, right.get(right_pointer).description, 0));
                        right_size--;
                        right_pointer++;
                    }

                    //answ_postings has the solution.
                    //bind the objcts together.
                    answ_rec.posting_list = answ_postings;
                    answ.term = "ANS";
                    answ.record = answ_rec;


                } else if (tokens_records.get(i - 1).record == null && tokens_records.get(i + 1).record == null) {
                    //both null so return null.
                    answ.term = "ANS";
                    answ.record = null;

                } else if (tokens_records.get(i - 1).record == null && tokens_records.get(i + 1).record != null) {
                    //left is null so add only right's postings.
                    answ_rec = tokens_records.get(i + 1).record;
                    answ.term = "ANS";
                    answ.record = answ_rec;
                } else if (tokens_records.get(i - 1).record != null && tokens_records.get(i + 1).record == null) {
                    //right is null so add only left's postings.
                    answ_rec = tokens_records.get(i - 1).record;
                    answ.term = "ANS";
                    answ.record = answ_rec;
                }
                //finally place answer ANS to right position and remove the appropriate ones.
                tokens_records.add(i - 1, answ);
                tokens_records.remove(i);
                tokens_records.remove(i);
                tokens_records.remove(i);
                //set lookup for or's at the beginning.
                i = 0;
            }
        }
        //ORs are evaluated

        //The final solution has been computed.
        //proceed by making some checks.
        //the arraylist tokens_records has the answer and it must be only one element.
        if (tokens_records.size() > 1) {
            System.out.println(tokens_records.size());
            System.out.println("SOMETHING GONE WRONG IN COMPUTING SOLUTION!!!");
            System.exit(0);
        } else if (tokens_records.get(0).record == null || tokens_records.get(0).record.posting_list == null || tokens_records.get(0).record.posting_list.size() == 0) {
            System.out.println();
            System.out.println("There are NO RESULTS regarding your query!!!");
            //end timer.
            long elapsedTime = System.nanoTime() - start;
            System.out.println();
            System.out.println("The search took: " + elapsedTime + " nanoseconds.");
        } else {
            System.out.println("THE ANSWER HAS BEEN COMPUTED SUCCESSFULLY!!!");
            System.out.println("Here are the results:");
            System.out.println("----------------");
            for (Posting p : tokens_records.get(0).record.posting_list) {
                System.out.println("Document id: " + p.doc_id);
                System.out.println("Description: " + p.description);
                System.out.println("----------------");
            }
            //end timer.
            long elapsedTime = System.nanoTime() - start;
            System.out.println("The search took: " + elapsedTime + " nanoseconds.");
        }
    }
}
