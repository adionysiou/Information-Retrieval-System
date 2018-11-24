/**
 * This class is used to represent a Record that has the frequency of a term found and also
 * an arraylist that contains objects of type Posting. This class is used as the value attribute
 * of Treemap<String,Record>.
 *
 * @author Antreas Dionysiou
 * @since 30/9/2018
 */

import java.util.ArrayList;
import java.io.Serializable;

public class Record implements Serializable {

    int frequency=0;
    //arraylist of type posting.
    ArrayList<Posting> posting_list = new ArrayList<Posting>();

    /**
     * Constructor 1.
     *
     * @param freq The frequency of term found in documents.
     * @param position The position of term found in the document id docid.
     * @param docid The document's id.
     * @param description The document's wine review.
     */
    public Record(int freq,int position,int docid,String description){
        this.frequency=freq;
        this.posting_list.add(new Posting(docid,description,position));

    }

    /**
     * Constructor 2.
     */
    public Record(){

    }
}
