import bridges.base.SLelement;
import bridges.connect.Bridges;
import java.util.Iterator;


public class tut_sllist_p1
{
    public static void main(String[] args) throws Exception
    {
            Bridges bridges = new Bridges(1,"RamleyHirn","575356762377");

            bridges.setTitle("A Single Linked List Example");

            bridges.setDescription("A singly linked list of 4 nodes with names; the nodes in this example use string as the generic type");

            SLelement<String>  st0 = new SLelement<String> ("Gretel Chaney");
            SLelement<String>  st1 = new SLelement<String> ("Lamont Kyler");
            SLelement<String>  st2 = new SLelement<String> ("Gladys Serino");
            SLelement<String>  st3 = new SLelement<String> ("Karol Soderman");
            SLelement<String>  st4 = new SLelement<String> ("Starr McGinn");

            st0.setNext(st1);
            st1.setNext(st2);
            st2.setNext(st3);
            st3.setNext(st4);

            st0.setLabel(st0.getValue());
            st1.setLabel(st1.getValue());
            st2.setLabel(st2.getValue());
            st3.setLabel(st3.getValue());
            st4.setLabel(st4.getValue());

            bridges.setDataStructure(st0);

            bridges.visualize();

    }
}
