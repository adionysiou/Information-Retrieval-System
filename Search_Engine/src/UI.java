/**
 * This class is used to represent the User Interface for the wines reviews' search engine.
 *
 * @author Antreas Dionysiou
 * @since 30/9/2018
 */

import java.util.*;

public class UI {

    /**
     * This function is used to check the validity of the user's choice for the menu action to perform.
     * @return int The menu selection.
     *
     */
    public static int get_input(){

        //open scanner to read user's choice.
        Scanner scan= new Scanner(System.in);
        String line="";
        int choice=0;
        //loop until correct choice is given.
        while (true) {

            //read user's choice.
            if (scan.hasNext()) {
                line = scan.next();
            }
            if (line.length()>2){
                System.out.println("Invalid menu choice!!! Please try again!!!");
                continue;
            }
            else if (line.charAt(0)<'1'||line.charAt(0)>'9'){
                System.out.println("Invalid menu choice!!! Please try again!!!");
                continue;
            }
            else if (line.length()==2&&(line.charAt(1)<'0'||line.charAt(1)>'1')){
                //if more options added here then modify 3rd argument of if '1'.
                System.out.println("Invalid menu choice!!! Please try again!!!");
                continue;
            }
            else {
                //correct input given i.e. 1-9, so break loop.
                choice= Integer.parseInt(line);
                break;
            }
        }
        return choice;
    }

    /**
     * This function is used to display the main menu on user's console.
     */
    public static void show_menu(){
        System.out.println();
        System.out.println("Main menu: (Select an option by giving the number of bullet and pressing enter.)");
        System.out.println("----------");
        System.out.println("1. Create new collection (Type \'1\').");
        System.out.println("2. Delete a collection (Type \'2\').");
        System.out.println("3. Insert a document to a specified collection (Type \'3\').");
        System.out.println("4. Delete a document from a specified collection (Type \'4\').");
        System.out.println("5. Insert multiple documents to a collection, from a specified folder (Type \'5\').");
        System.out.println("6. Print contents (files) of a specified collection (Type \'6\').");
        System.out.println("7. Print the dictionary of a specified collection (Type \'7\').");
        System.out.println("8. Index retrieval for a specified collection (Type \'8\').");
        System.out.println("9. Insert new term to a collection's dictionary (Type \'9\').");
        System.out.println("10. Search (Type \'10\').");
        System.out.println("11. Exit (Type \'11\').");
    }

    /**
     * This function is the main function that Dionysos winery search engine starts.
     * @param args
     */
    public static void main (String[] args){

        System.out.println();
        System.out.println("Welcome to wine search engine.");
        show_menu();

        //read user's action choise.
        int choice = get_input();

        //create index module instance.
        Indexing_Module index_module = new Indexing_Module();

        Boolean_Module boolean_module = new Boolean_Module();

        //loop until exit (11) given as input.
        while (choice!=11){
            //perform the appropriate action based on user's choice.
            switch (choice) {
                case 1:  index_module.create_new_collection();
                    break;
                case 2:  index_module.delete_collection();
                    break;
                case 3:  index_module.insert_doc_to_collection();
                    break;
                case 4:  index_module.delete_doc_from_collection();
                    break;
                case 5:  index_module.insert_docs_from_directory_to_collection();
                    break;
                case 6:  index_module.print_docs_of_a_collection();
                    break;
                case 7:  index_module.print_dictionary_of_a_collection();
                    break;
                case 8:  index_module.retrieve_index_of_collection();
                    break;
                case 9:  index_module.insert_term_to_collection();
                    break;
                case 10: boolean_module.search();
                    break;
                default:
                    System.out.println("Wrong choice!!!");
                    break;
            }

            //ask for user's next action.
            System.out.println();
            System.out.println("Select next action to perform.");
            //show menu.
            show_menu();
            //get input.
            choice=get_input();
        }
        System.out.println();
        System.out.println("Thank you for using our system!!! :)");
    }
}
