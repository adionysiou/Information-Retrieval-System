/**
 * This class is used to represent the Index Module responsible for creating, maintaining and updating
 * the index of each collection in Dionysos directory.
 *
 * @author Antreas Dionysiou
 * @since 30/9/2018
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Indexing_Module {

    /**
     * Constructor.
     */
    public Indexing_Module() {

    }

    /**
     * This function is used to retrive the stop words from stoplist.txt.
     *
     * @return ArrayList<String> An array list containing the stop words retrieved.
     */
    public static ArrayList<String> retr_stop_words() {
        ArrayList<String> stopwords = new ArrayList<String>();
        File stoplist = new File("stoplist.txt");
        Scanner scan;
        try {
            scan = new Scanner(stoplist);
            String line = "";
            while (scan.hasNextLine()) {
                //read next stop word.
                line = scan.nextLine();
                //store it to arraylist with stop words.
                stopwords.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }

        //return arraylist.
        return stopwords;
    }

    /**
     * This method is used to create a new collection (directory) in Dionysos folder.
     *
     * @return void
     */
    public void create_new_collection() {

        System.out.println("Please give the name for the new collection to be created:");
        //open scanner to read user's collection name preference.
        Scanner scan1 = new Scanner(System.in);
        String collection_name = "";
        do {
            if (scan1.hasNextLine()) {
                collection_name = scan1.nextLine();
            }
            StringTokenizer st = new StringTokenizer(collection_name, " ");
            if (st.countTokens() > 1) {
                System.out.println("Please give one term as the collection name!!! Give the name again!!!");
                continue;
            }
            //check if collection (directory) already exists.
            File dir = new File("Dionysos/" + collection_name);
            // Tests whether the directory denoted by this abstract pathname exists.
            if (dir.exists()) {
                System.out.println("The collection already exists!!! Please give a different name!!!");
                continue;
            }
            //Else name is okay so proceed.
            break;
        } while (true);

        //create the new directory.
        File file = new File("Dionysos/" + collection_name);
        if (file.mkdir()) {
            System.out.println("Directory with name " + collection_name + " has been created!");
            System.out.println();
        } else {
            System.out.println("Failed to create directory!");
            System.out.println();
        }

    }

    /**
     * This method is used to delete directories with subdirectories and files.
     *
     * @param file The file to be deleted.
     * @throws IOException
     * @return void
     */
    public void delete(File file)
            throws IOException {

        if (file.isDirectory()) {

            //directory is empty, then delete it
            if (file.list().length == 0) {

                file.delete();
                System.out.println("Directory is deleted : "
                        + file.getAbsolutePath());

            } else {

                //list all the directory contents
                String files[] = file.list();

                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);

                    //recursive delete
                    delete(fileDelete);
                }

                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                    System.out.println("Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }

        } else {
            //if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    /**
     * This method is used for deleting a specified collection (directory).
     *
     * @return void
     */
    public void delete_collection() {

        System.out.println("Please give the name of the collection to be deleted:");
        //open scanner to read user's collection name preference.
        Scanner scan1 = new Scanner(System.in);
        String collection_name = "";
        do {
            if (scan1.hasNextLine()) {
                collection_name = scan1.nextLine();
            }
            StringTokenizer st = new StringTokenizer(collection_name, " ");
            if (st.countTokens() > 1) {
                System.out.println("Please give one term as the collection name!!! Give the name again!!!");
                continue;
            }
            //check if collection (directory) exists.
            File dir = new File("Dionysos/" + collection_name);
            // Tests whether the directory denoted by this abstract pathname exists.
            if (!dir.exists()) {
                System.out.println("The collection with name: " + collection_name + " does not exist in Dionysos directory!!! Please give the name of the collection again!!!");
                continue;
            } else {
                try {
                    //delete folder with all files
                    delete(new File("Dionysos/" + collection_name));
                    System.out.println("Collection " + collection_name + " deleted successfully from Dionysos directory!");
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Couldn't delete files!!!");
                    System.exit(0);
                }

            }
        } while (true);
    }

    /**
     * This method is used to check if a directory exists.
     *
     * @param collection_name The collection name (directory folder).
     * @return Boolean True if exists, False otherwise.
     */
    public boolean check_directory(String collection_name) {
        //check if collection (directory) exists.
        File dir = new File("Dionysos/" + collection_name);
        // Tests whether the directory denoted by this abstract pathname exists.
        if (!dir.exists()) {
            System.out.println("The collection with name: " + collection_name + " does not exist in Dionysos directory! Please give the collection's name again!");
            return false;
        } else {
            //directory exists so return true.
            return true;
        }
    }

    /**
     * This method is used to inserting a document in a specified collection (directory).
     *
     * @return void
     */
    public void insert_doc_to_collection() {

        System.out.println("Please give the name of the collection for the document to be added in:");
        //open scanner to read user's collection name preference.
        Scanner scan1 = new Scanner(System.in);
        String collection_name = "";
        String file_path_tobemoved = "";
        do {
            if (scan1.hasNextLine()) {
                collection_name = scan1.nextLine();
            }
            StringTokenizer st = new StringTokenizer(collection_name, " ");
            if (st.countTokens() > 1) {
                System.out.println("Please give one term as the collection name!!! Give the name again!!!");
                continue;
            }
            //check if collection (directory) exists.
            File dir = new File("Dionysos/" + collection_name);
            // Tests whether the directory denoted by this abstract pathname exists.
            if (!dir.exists()) {
                System.out.println("The collection with name: " + collection_name + " does not exist in Dionysos directory! Please give the name again!");
                continue;
            } else {
                //ask for path of file to be moved.
                System.out.println("Please enter the path of the document to be moved to Dionysos/" + collection_name + " collection.");

                //move file from current directory to target directory.
                file_path_tobemoved = "";
                if (scan1.hasNextLine()) {
                    file_path_tobemoved = scan1.nextLine();
                }

                //make file path to PATH type
                Path afile_path = Paths.get(file_path_tobemoved);

                //try to move the file to dir folder and replace if the file already exists.
                try {
                    Files.move(afile_path, dir.toPath().resolve(afile_path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println();
                    System.out.println("The file '" + afile_path.getFileName() + "' has been moved to 'Dionysos/" + collection_name + "' successfully.");
                    //update index
                    Indexing_Module index_module = new Indexing_Module();
                    TreeMap<String, Record> index = index_module.create_index(collection_name);
                    //serealize index
                    index_module.serialize_treemap(collection_name, index, 1);
                    //new index serealized successfully.

                    break;
                } catch (IOException e) {
                    System.out.println("The file: " +afile_path.getFileName()+" does not exist in the path given! Please start over and give the collection's name again!");
                    //e.printStackTrace();
                }
            }
        } while (true);
        System.out.println("File " + file_path_tobemoved + " has been successfully moved to Dionysos/" + collection_name + " collection!");
    }

    /**
     * This method is used for deleting a document from a specified collection (directory).
     *
     * @return void
     */
    public void delete_doc_from_collection() {

        System.out.println("Please give the name of the file to be deleted from a specific collection:");
        //open scanner to read user's file name preference to delete.
        Scanner scan1 = new Scanner(System.in);
        String filename = "";
        String collection = "";
        int flag = 0;
        do {
            if (scan1.hasNextLine()) {
                filename = scan1.nextLine();
            }
            StringTokenizer st = new StringTokenizer(filename, " ");
            if (st.countTokens() > 1) {
                System.out.println("Please give one term as the file name!!! Give the name again!!!");
                continue;
            } else {

                //ask for collection that file is in.
                System.out.println("Please enter the collection name that contains the file \'" + filename + "\' to be deleted.");

                do {
                    if (scan1.hasNextLine()) {
                        collection = scan1.nextLine();
                    }
                    st = new StringTokenizer(collection, " ");
                    if (st.countTokens() > 1) {
                        System.out.println("Please give one term for the collection name!!! Give the collection name again!!!");
                        continue;
                    }
                    //check if file exists.
                    File coll_dir = new File("Dionysos/" + collection);
                    // Tests whether the collection denoted by this abstract pathname exists.
                    if (!coll_dir.exists()) {
                        System.out.println("The collection with name: " + collection + " does not exist! Please give the collection name again!");
                        continue;
                    } else {
                        //check if file is indeed in the given collection.
                        File given_file_dir = new File("Dionysos/" + collection + "/" + filename);
                        if (!given_file_dir.exists()) {
                            System.out.println("The file: '" + filename + "' does not exist in collection: Dionysos/" + collection + " ! Please start over by giving the file name again!");
                            break;
                        }
                        //else file filename is indeed in collection given so delete it.
                        if (given_file_dir.delete()) {
                            System.out.println();
                            System.out.println("File '" + given_file_dir.getName() + "' has been deleted successfully from Dionysos/" + collection + " collection.");
                            //update index
                            Indexing_Module index_module = new Indexing_Module();
                            TreeMap<String, Record> index = index_module.create_index(collection);
                            //serealize index
                            index_module.serialize_treemap(collection, index, 1);
                            //new index serealized successfully.
                            flag = 1;
                            break;

                        } else {
                            System.out.println("Delete operation is failed.");
                        }
                    }
                } while (true);
            }
            //if file deleted then break loop.
            if (flag == 1) {
                break;
            }
        } while (true);
    }

    /**
     * This method is used for inserting multiple docoments from as specified directory to a specified collection.
     *
     * @return void
     */
    public void insert_docs_from_directory_to_collection() {

        System.out.println("Please give the name of the collection for the documents to be added in:");
        //open scanner to read user's collection name preference.
        Scanner scan1 = new Scanner(System.in);
        String collection_name = "";
        String path_to_files_dir = "";
        do {
            if (scan1.hasNextLine()) {
                collection_name = scan1.nextLine();
            }
            StringTokenizer st = new StringTokenizer(collection_name, " ");
            if (st.countTokens() > 1) {
                System.out.println("Please give one term as the collection name!!! Give the collection name again!!!");
                continue;
            }
            //check if collection (directory) exists.
            File dir = new File("Dionysos/" + collection_name);
            // Tests whether the directory denoted by this abstract pathname exists.
            if (!dir.exists()) {
                System.out.println("The collection with name: " + collection_name + " does not exist in Dionysos directory! Please give the collection name again!");
                continue;
            } else {
                //collection exists
                //ask for path of the files.
                System.out.println("Please enter the path of the directory hosting the files to be moved to Dionysos/" + collection_name + " collection.");

                //move file from current directory to target directory.
                path_to_files_dir = "";
                if (scan1.hasNextLine()) {
                    path_to_files_dir = scan1.nextLine();
                }

                //check if directory with files exists.
                File files_dir = new File(path_to_files_dir);
                // Tests whether the directory denoted by this abstract pathname exists.
                if (!files_dir.exists()) {
                    System.out.println("The directory with path: '" + files_dir.getPath() + "' does not exist! Please start over by giving the collection's name again!");
                    continue;
                }

                //else directory with files exists.
                File paths[];

                // returns pathnames for files and directory
                paths = files_dir.listFiles();

                // for each pathname in pathname array
                //move the files to the new destination collection
                int counter = 0;
                for (File path : paths) {

                    //skip serialized indexes.
                    if (path.getName().contains(".ser")) {
                        continue;
                    }
                    //skip .DS_Store FILE (exist on MAC OS)
                    if (path.getName().contains(".DS_Store")) {
                        continue;
                    }

                    //try to move the file to collection folder and replace if the file already exists.
                    try {
                        Files.move(path.toPath(), dir.toPath().resolve(path.getName()), StandardCopyOption.REPLACE_EXISTING);
                        counter++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println();
                System.out.println(counter + " files have been moved in collection 'Dionysos/" + collection_name + "' successfully!");
                //update index
                Indexing_Module index_module = new Indexing_Module();
                TreeMap<String, Record> index = index_module.create_index(collection_name);
                //serealize index
                index_module.serialize_treemap(collection_name, index, 1);
                //new index serealized successfully.
                break;
            }
        } while (true);
    }

    /**
     * This method is used for printing documents of a specified collection.
     *
     * @return void
     */
    public void print_docs_of_a_collection() {

        System.out.println("Please give the name of the collection to print the containing documents:");
        //open scanner to read user's collection name preference.
        Scanner scan1 = new Scanner(System.in);
        String collection_name = "";
        do {
            if (scan1.hasNextLine()) {
                collection_name = scan1.nextLine();
            }
            StringTokenizer st = new StringTokenizer(collection_name, " ");
            if (st.countTokens() > 1) {
                System.out.println("Please give one term as the collection name!!! Give the name again!!!");
                continue;
            }
            //check if collection (directory) exists.
            File dir = new File("Dionysos/" + collection_name);
            // Tests whether the directory denoted by this abstract pathname exists.
            if (!dir.exists()) {
                System.out.println("The collection with name: " + collection_name + " does not exist in Dionysos directory!!! Please give the collection's name again!!!");
                continue;
            } else {
                //collection exists so print all the containing files.
                File paths[];

                // returns pathnames for files and directory
                paths = dir.listFiles();

                System.out.println();
                System.out.println("The collection '"+collection_name+"' contains the following files:");
                System.out.println("------------------------------------------------------------------");
                // for each pathname in pathname array
                for (File path : paths) {

                    System.out.println(path.getName());
                }
                System.out.println("------------------------------------------------------------------");
                break;
            }
        } while (true);
    }

    /**
     * This method is used to check if serealized treemap exist and return it if exists.
     *
     * @param collection_name The collection name to search in
     * @return TreeMap<String,Record> The index retrieved (null if no serialized index found).
     */
    public TreeMap<String, Record> find_serealized_treemap(String collection_name) {
        //no new files inserted so check if serialized index exists.
        File f = new File("Dionysos/" + collection_name + "/" + collection_name + ".ser");
        TreeMap<String, Record> index = null;
        if (f.exists()) {
            //serialized object exists so deserialize it.
            System.out.println();
            System.out.println("Serialized index exists, so please wait for the deserialization process to finish.");
            System.out.println();
            index = null;
            try {
                FileInputStream fileIn = new FileInputStream("Dionysos/" + collection_name + "/" + collection_name + ".ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                index = (TreeMap<String, Record>) in.readObject();
                in.close();
                fileIn.close();
            } catch (IOException i) {
                i.printStackTrace();
                return null;
            } catch (ClassNotFoundException c) {
                System.out.println("Treemap class not found");
                c.printStackTrace();
                return null;
            }
            //object deserialized.
            System.out.println("The index for the collection '"+collection_name+"' has been deserialized successfully.");
            return index;
        } else {
            //serialized tree does not exist.
            System.out.println();
            System.out.println("Serialized index does not exist so a new one will be created. Please wait a little bit.");
            System.out.println();
            return null;
        }
    }

    /**
     * This method is used to retrieve the index of a specified collection.
     *
     * @return void
     */
    public void retrieve_index_of_collection() {

        System.out.println("Please give the name of the collection to retrieve the index:");
        //open scanner to read user's collection name preference.
        Scanner scan1 = new Scanner(System.in);
        String collection_name = "";
        TreeMap<String, Record> index =null;
        do {
            if (scan1.hasNextLine()) {
                collection_name = scan1.nextLine();
            }
            StringTokenizer st = new StringTokenizer(collection_name, " ");
            if (st.countTokens() > 1) {
                System.out.println("Please give one term as the collection name!!! Give the name again!!!");
                continue;
            }
            //check if collection (directory) exists.
            File dir = new File("Dionysos/" + collection_name);
            // Tests whether the directory denoted by this abstract pathname exists.
            if (!dir.exists()) {
                System.out.println("The collection with name: " + collection_name + " does not exist in Dionysos directory!!! Please give the collection's name again!!!");
                continue;
            } else {
                //collection exists so retrieve the index if exists.
                //check if serialized object exists and deserialize it.
                index = this.find_serealized_treemap(collection_name);
                if (index!=null){
                    //serialized treemap existed and deserialized in index variable, so break loop.
                    break;
                } else if (index==null){
                    //serealized index didn't exist so create a new one.
                    index = this.create_index(collection_name);
                    //index retrieved successfully so break the while(true) loop and serealize the new index.
                    this.serialize_treemap(collection_name, index, 1);
                    break;
                }
            }
        } while (true);

        //print dictionary of index.
        Set<String> keys = index.keySet();
        System.out.println();
        System.out.println("Index of collection: Dionysos/" + collection_name);
        System.out.println("--------------------");
        for (String key : keys) {
            //System.out.print("Term: "+key + " , Frequency:" + index.get(key).frequency + " , Postings' IDs: ");
            System.out.print("Term: "+key + " , Frequency:" + index.get(key).frequency + " , Postings{");

            for (Posting i : index.get(key).posting_list) {
                System.out.print(" Doc_id: "+i.doc_id+" Positions: ");
                for (int pos:i.positions){
                    System.out.print(pos+",");
                }
                //System.out.print(i.doc_id + ", ");
            }
            System.out.println("}");
        }
        System.out.println("--------------------");
    }

    /**
     * This method is used for printing the dictionary (words) of a specified collection.
     *
     * @return void
     */
    public void print_dictionary_of_a_collection() {

        System.out.println("Please give the name of the collection to print the dictionary words:");
        //open scanner to read user's collection name preference.
        Scanner scan1 = new Scanner(System.in);
        TreeMap<String, Record> index = null;
        String collection_name = "";
        do {
            if (scan1.hasNextLine()) {
                collection_name = scan1.nextLine();
            }
            StringTokenizer st = new StringTokenizer(collection_name, " ");
            if (st.countTokens() > 1) {
                System.out.println("Please give one term as the collection name!!! Give the name again!!!");
                continue;
            }
            //check if collection (directory) exists.
            File dir = new File("Dionysos/" + collection_name);
            // Tests whether the directory denoted by this abstract pathname exists.
            if (!dir.exists()) {
                System.out.println("The collection with name: " + collection_name + " does not exist in Dionysos directory!!! Please give the collection's name again!!!");
                continue;
            } else {
                //collection exists so retrieve the index if exists.
                //check if serialized object exists and deserialize it.
                index = this.find_serealized_treemap(collection_name);
                if (index!=null){
                    //serialized treemap existed and deserialized in index variable, so break loop.
                    break;
                }
                else if (index==null){
                    //Serealized index didn't exist so create a new one.
                    index = this.create_index(collection_name);
                    //index retrieved successfully so break the while(true) loop but before that serialize the new index.
                    this.serialize_treemap(collection_name, index, 1);
                    break;
                }
            }
        } while (true);
        //print dictionary of index.
        Set<String> keys = index.keySet();
        System.out.println();
        System.out.println("Dictionary of collection: Dionysos/" + collection_name);
        System.out.println("-------------------------");
        for (String key : keys) {
            System.out.println(key);
        }
        System.out.println("-------------------------");
    }

    /**
     * This method is used to create an index of a specified collection.
     *
     * @param collection_name The collection name in Dionysos directory.
     * @return TreeMap<String, Record> The treemap created.
     */
    public TreeMap<String, Record> create_index(String collection_name) {

        File dir = new File("Dionysos/" + collection_name);
        StringTokenizer st;
        TreeMap<String, Record> index = new TreeMap<String, Record>();

        //retrive the stop words.
        ArrayList<String> stopwords = retr_stop_words();

        //collection exists so print all the containing files.
        File paths[];

        // returns pathnames for files and directory
        paths = dir.listFiles();

        // for each pathname in pathname array
        String description = "";
        StringTokenizer descr_toke;
        for (File path : paths) {

            //skip serialized indexes.
            if (path.getName().contains(".ser")) {
                continue;
            }

            //if serialized file found, skip it!
            if (path.getName().replace(".txt", "").equals(collection_name + ".ser")) {
                continue;
            } else if (path.getName().equals(".DS_Store")) {
                //skip .DS_Store a file for mac os.
                continue;
            }

            int docid = Integer.parseInt(path.getName().replace(".txt", ""));
            try {
                Scanner scan = new Scanner(path);
                String line = scan.nextLine();
                st = new StringTokenizer(line, "\"");
                st.nextToken();
                if (!st.hasMoreTokens()) {
                    //the file does not contain " to separate the description
                    //separate them twice
                    String[] line_ded = line.split("\\.,");
                    line = line_ded[0];
                    int commaflag = 2;
                    int expoint = 0;
                    for (expoint = 0; expoint < line.length(); expoint++) {
                        if (line.charAt(expoint) == ',') {
                            commaflag--;
                        }
                        if (commaflag == 0) {
                            expoint++;
                            break;
                        }
                    }
                    line = line.substring(expoint) + ".";
                    //System.out.println(line);
                    //System.out.println(docid);
                    description = line;
                } else {
                    description = st.nextToken();
                }
                descr_toke = new StringTokenizer(description, " /-");
                //iterate on all words
                String token = "";
                int skiptoken_flag = 0;
                int position_counter = 0;
                Stemmer stemmer = new Stemmer();
                try {
                    while (true) {
                        skiptoken_flag = 0;
                        token = descr_toke.nextToken();
                        token = token.replaceAll("[^a-zA-Z0-9]","");

                        token = token.toLowerCase();

                        //remove stop words
                        for (String word : stopwords) {
                            //if token is a stop word then throw skip it.
                            if (word.equals(token)) {
                                skiptoken_flag = 1;
                                break;
                            }
                        }
                        if (skiptoken_flag == 1) {
                            //the token is a stop word so skip it.
                            //increase position counter.
                            position_counter++;
                            continue;
                        }
                        //else the token is not a stop word, stemm it and add it to the treemap.

                        //System.out.println("Normal token: "+token);
                        //stemm token
                        char[] tok1 = token.toCharArray();
                        stemmer.add(tok1, tok1.length);
                        stemmer.stem();
                        //System.out.println("Stemmed token: "+stemmer.toString());
                        token = stemmer.toString();

                        //first check if term is in treemap.
                        Record token_postinglist = index.get(token);
                        if (token_postinglist == null) {
                            //if null then token does not exist in treemap sto add it.
                            index.put(token, new Record(1, position_counter, docid, description));
                        } else {
                            //else token exists in treemap so check if docid already has a posting.
                            //token_postinglist.frequency++;

                            if (token_postinglist.posting_list.size() == 0) {
                                //there is not any docid so add it.
                                token_postinglist.posting_list.add(new Posting(docid, description, position_counter));
                                token_postinglist.frequency=1;

                            } else {
                                int inserted_flag = 0;
                                //other docs_ids exist so iterate to place it sorted.
                                for (int i = 0; i < token_postinglist.posting_list.size(); i++) {
                                    //first check if the same docid exist/
                                    if (token_postinglist.posting_list.get(i).doc_id == docid) {
                                        //a same docid exists so just add the positioning.
                                        token_postinglist.posting_list.get(i).positions.add(position_counter);
                                        inserted_flag = 1;
                                        break;
                                    } else if (token_postinglist.posting_list.get(i).doc_id > docid) {
                                        //the docid is smaller so it has to be placed before the position i.
                                        token_postinglist.posting_list.add(i, new Posting(docid, description, position_counter));
                                        inserted_flag = 1;
                                        //increase frequency as another doc contains the term.
                                        token_postinglist.frequency++;
                                        break;
                                    }
                                }
                                if (inserted_flag == 0) {
                                    //the Posting has not been inserted as it has the bigest docid so place it last.
                                    token_postinglist.posting_list.add(new Posting(docid, description, position_counter));
                                    //increase frequency as another doc contains the term.
                                    token_postinglist.frequency++;
                                }
                            }
                        }
                        //increase position counter.
                        position_counter++;
                    }
                } catch (NoSuchElementException e) {
                    //System.out.println("END");
                }
                //close scan to close the file stream to not full memory.
                scan.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        //index retrieved successfully so break the while(true) loop.
        return index;
    }

    /**
     * This method is used for serealizing a given inded.
     *
     * @param collection_name The collection name.
     * @param index The index to be serialized.
     * @param flag Flag for printing info in console.
     * @return void
     */
    public void serialize_treemap(String collection_name, TreeMap<String, Record> index, int flag) {

        //serialize the object before function closes.
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("Dionysos/" + collection_name + "/" + collection_name + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(index);
            out.close();
            fileOut.close();
            if (flag == 1) {
                System.out.println("");
                System.out.printf("Serialized data is saved in Dionysos/" + collection_name + "/" + collection_name + ".ser");
                System.out.println("");
            }
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * This function is used to insert a term in a specified collection and serialize the new object.
     *
     * @return void
     */
    public void insert_term_to_collection() {

        System.out.println("Please give the name of the collection for the term to be added:");
        //open scanner to read user's collection name preference.
        Scanner scan1 = new Scanner(System.in);
        String collection_name = "";
        TreeMap<String, Record> index;
        String file_path_tobemoved = "";
        String line = "";
        do {
            if (scan1.hasNextLine()) {
                collection_name = scan1.nextLine();
            }
            StringTokenizer st = new StringTokenizer(collection_name, " ");
            if (st.countTokens() > 1) {
                System.out.println("Please give one term as the collection name!!! Give the name again!!!");
                continue;
            }
            //check if collection (directory) exists.
            File dir = new File("Dionysos/" + collection_name);
            // Tests whether the directory denoted by this abstract pathname exists.
            if (!dir.exists()) {
                System.out.println("The collection with name: " + collection_name + " does not exist in Dionysos directory!!! Please give the name again!");
                continue;
            } else {
                //collection given exists.
                //check if there is a serialized treemap.
                File f = new File("Dionysos/" + collection_name + "/" + collection_name + ".ser");
                if (f.exists()) {
                    //serialized object exists so deserialize it.
                    index = null;
                    try {
                        FileInputStream fileIn = new FileInputStream("Dionysos/" + collection_name + "/" + collection_name + ".ser");
                        ObjectInputStream in = new ObjectInputStream(fileIn);
                        index = (TreeMap<String, Record>) in.readObject();
                        in.close();
                        fileIn.close();
                    } catch (IOException i) {
                        i.printStackTrace();
                        return;
                    } catch (ClassNotFoundException c) {
                        System.out.println("Treemap class not found");
                        c.printStackTrace();
                        return;
                    }
                    //object deserialized.

                } else {
                    //serialized treemap does not exist so create a treemap.
                    index = create_index(collection_name);
                }

                //ask for term to be inserted.
                do {
                    System.out.println("Please give the term you want to insert: (one at a time)");
                    if (scan1.hasNextLine()) {
                        line = scan1.nextLine();
                    }
                    StringTokenizer st1 = new StringTokenizer(line, " ");
                    if (st1.countTokens() > 1) {
                        System.out.println("Please give one term at a time!!!");
                        continue;
                    }
                    //check if term is stop word.
                    ArrayList<String> stopwords = retr_stop_words();
                    int stop_flag = 0;
                    for (String word : stopwords) {
                        if (word.equals(line)) {
                            System.out.println("The word you gave is a stop word!!!");
                            stop_flag = 1;
                            break;
                        }
                    }
                    if (stop_flag == 1) {
                        //stop word detected.
                        continue;
                    }
                    //else one term given so break loop.
                    break;
                } while (true);

                //make the letters lowercase
                line = line.toLowerCase();

                //stemm token
                Stemmer stemmer = new Stemmer();
                char[] tok1 = line.toCharArray();
                stemmer.add(tok1, tok1.length);
                stemmer.stem();
                //System.out.println("Stemmed token: "+stemmer.toString());
                line = stemmer.toString();

                //insert term
                index.put(line, new Record());
                System.out.println("Term " + line + " added to index of Dionysos/" + collection_name + " collection!");
                System.out.println("Finish adding extra terms to collections? 1:No 2:Yes");
                String option = "";

                if (scan1.hasNextLine()) {
                    option = scan1.nextLine();
                }
                while (option.length() > 1 || (option.charAt(0) != '1' && option.charAt(0) != '2')) {
                    System.out.println("Wrong input!!! Please give option again!!!");
                    System.out.println("Finish adding extra terms? (Type 1 for No, 2 for Yes).");
                    if (scan1.hasNextLine()) {
                        option = scan1.nextLine();
                    }
                }
                //check if finished adding terms. 1:No 2:Yes
                if (Integer.parseInt(option) == 2) {
                    //finished sto break loop.
                    break;
                } else if (Integer.parseInt(option) == 1) {
                    //serialize the new index consistiong of new terms added.
                    serialize_treemap(collection_name, index, 2);
                    //treemap serialized!!!
                    //not finished so ask again for new collection to add term.
                    System.out.println("Please give the name of the collection for the term to be added:");
                    continue;
                }
            }
        } while (true);
        //finished adding terms so serialize new treemap.
        serialize_treemap(collection_name, index, 1);
        //treemap serialized!!!
    }
}
