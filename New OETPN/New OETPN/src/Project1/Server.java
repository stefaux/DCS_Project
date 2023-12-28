package Project1;

import Components.*;
import DataObjects.DataFloat;
import DataObjects.DataTransfer;
import DataOnly.TransferOperation;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;
import PetriDataPackage.Place;

import javax.xml.crypto.Data;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args)
    {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Petri Server";
        pn.NetworkPort = 1081;

        DataFloat p0 = new DataFloat();
        p0.SetName("P0");
        p0.SetValue(1.0f);
        pn.PlaceList.add(p0);

        DataFloat p1_s = new DataFloat();
        p1_s.SetName("P1_S");
        pn.PlaceList.add(p1_s);

        DataFloat p2 = new DataFloat();
        p2.SetName("P2");
        pn.PlaceList.add(p2);

        DataTransfer p3_s = new DataTransfer();
        p3_s.SetName("P3_S");
        p3_s.Value = new TransferOperation("localhost", "1080", "P5_C");

        DataFloat subConstantValue1 = new DataFloat();
        subConstantValue1.SetName("subConstantValue1");
        subConstantValue1.SetValue(0.01f);
        pn.ConstantPlaceList.add(subConstantValue1);

        // T1
        PetriTransition t1 = new PetriTransition(pn);
        t1.SetName("T1");
        t1.InputPlaceName.add("P1_S");
        t1.InputPlaceName.add("P0");

        Condition T1Ct1 = new Condition(t1, "P0", TransitionCondition.NotNull);
        Condition T1Ct2 = new Condition(t1, "P1_S", TransitionCondition.NotNull);
        T1Ct1.SetNextCondition(LogicConnector.AND, T1Ct2);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;
        ArrayList<String> listInput = new ArrayList<String>();
        listInput.add("P1_S");
        listInput.add("subConstantValue1");
        grdT1.Activations.add(new Activation(t1, listInput, TransitionOperation.Prod, "P2"));
        t1.GuardMappingList.add(grdT1);

        t1.Delay = 0;
        pn.Transitions.add(t1);

        // T2
        PetriTransition t2 = new PetriTransition(pn);
        t2.SetName("T2");
        t2.InputPlaceName.add("P2");

        Condition T2Ct1 = new Condition(t2, "P2", TransitionCondition.NotNull);

        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition = T2Ct1;
        grdT2.Activations.add(new Activation(t2, "P2", TransitionOperation.SendOverNetwork, "P3"));
        grdT2.Activations.add(new Activation(t2, "P2", TransitionOperation.Move, "P2"));

        t2.GuardMappingList.add(grdT2);
        t2.Delay = 0;
        pn.Transitions.add(t2);

        //-----------------------------------------------
        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}
