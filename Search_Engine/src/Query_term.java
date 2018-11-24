/**
 * This class is used to represent an object of the query arraylist that holds the query
 * tokens that user gave to search. It is also used as the evaluation of the solution for the
 * query.
 *
 * @author Antreas Dionysiou
 * @since 30/9/2018
 */

import java.util.TreeMap;

public class Query_term {

    //The term of query gave by user. If token is subanswer then term equals "ANS".
    String term="";
    //The record object containing the postings.
    Record record = null;

    /**
     * Constructor 1.
     *
     * @param term The term that given as query, or ANS if it is an evaluation of answer.
     * @param index The treemap index.
     */
    public Query_term(String term,TreeMap<String,Record> index){
        this.term = term;
        if (term.equals("AND")||term.equals("OR")||term.equals("NOT")){
            this.record = null;
        }
        else {
            //return record (with postings) for the term in record.
            record = index.get(term);
        }

    }

    /**
     * Constructor 2.
     */
    public Query_term(){

    }

}
