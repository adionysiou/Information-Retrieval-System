/**
 * This class is used to represent a Posing object  contained in the a Record's posting list.
 * It has three attributes, the document id (doc_id), an arraylist holding the positions of the term
 * found in the doc_id document, and finally the description attribute that contains the description of
 * the wine review.
 *
 * @author Antreas Dionysiou
 * @since 30/9/2018
 */

import java.util.ArrayList;
import java.io.Serializable;

public class Posting implements Serializable{

    //document's id.
    int doc_id=0;
    //positions, that the term found in this document.
    ArrayList<Integer> positions = new ArrayList<Integer>();
    //description
    String description="";

    /**
     * Constructor.
     *
     * @param docid The document's id.
     * @param desc The document's description about wine review.
     * @param position An arraylist that contains the positions of the term found in the document id.
     *
     */
    public Posting (int docid,String desc,int position){
        //set document id.
        this.doc_id=docid;
        //set document description.
        this.description = desc;
        //add positon of token.
        positions.add(position);
    }

}
