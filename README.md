# BooleanSearchEngine (Information Retrieval System)
This project represents a simple, yet robust, Boolean search engine (Information Retrieval System). The program (written in Java) represents a basic search engine that creates a positional inverted index for storing the documents data for each data collection. Furthermore, the user is able to execure Boolean queries (AND, OR, NOT) of arbitary size on a specific collection. This project has been created to represent the fundamental data structures and functions of a typical state-of-the-art searh engine. The search engine implementation has been tested on a benchmark dataset regarding wine reviews retrieved fromm Kaggle (https://www.kaggle.com/zynicide/wine-reviews/data). Please note that we are interested on indexing only the data in description field (Dionysos wine review). The first 51 wine reviews are located in Dionysos/winedata directory.

The program displays the following action menu once its started.
---------------------------------------------------------------------------------

Welcome to wine search engine.
Main menu: (Select an option by giving the number of bullet and pressing enter.)
1. Create new collection (Type '1').
2. Delete a collection (Type '2').
3. Insert a document to a specified collection (Type '3').
4. Delete a document from a specified collection (Type '4').
5. Insert multiple documents to a collection, from a specified folder (Type '5').
6. Print contents (files) of a specified collection (Type '6').
7. Print the dictionary of a specified collection (Type '7').
8. Index retrieval for a specified collection (Type '8').
9. Insert new term to a collection's dictionary (Type '9').
10. Search (Type '10').
11. Exit (Type '11').
---------------------------------------------------------------------------------
The program has the ability to serialize and deserialize the objects (positional inverted indexes) for later use.

The program is composed by 5 classes each one serving a different purpose.
- UI: Represents the User Interface.
- Steemer: Porter's Stemmer algorithm for stemming terms found in documents.
- Record: Represents a record in inverted index containing the frequency (# of documents containing the term) of a specific term as well as an ArrayList of type Posting objects.
- Posting: Represents a posting for a specific document. Contains the document's id, an ArrayList with the positions of the term found in the specific document, and the actual content of our search interest which is the description for a specific wine. 
- Indexing_Module: Represents the Index Module responsible for creating, maintaining and updating the index of each collection in Dionysos directory.
- Query_term: Represents a query term given by the user.
- Boolean_Module: Represents an arbitary size query Boolean evaluator (evaluates a query of Query_term objects).
---------------------------------------------------------------------------------
The stoplist.txt contains the stop words that are removed in tokenization process of documents.

The program is licenced under the GNU's general public licence.

The folder JavaDoc contains the Javadoc documentation for the project.
