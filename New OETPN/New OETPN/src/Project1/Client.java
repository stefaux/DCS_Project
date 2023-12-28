package Project1;

import Components.*;
import DataObjects.DataFloat;
import DataObjects.DataTransfer;
import DataOnly.TransferOperation;
import Enumerations.LogicConnector;
import Enumerations.TransitionCondition;
import Enumerations.TransitionOperation;
import PetriDataPackage.Guard;

public class Client {
    public static void main(String[] args)
    {
        PetriNet pn = new PetriNet();
        pn.PetriNetName = "Petri Client";
        pn.NetworkPort = 1080;

        // Places
        DataFloat p0 = new DataFloat();
        p0.SetName("P0");
        p0.SetValue(1.0f);
        pn.PlaceList.add(p0);

        DataFloat p1 = new DataFloat();
        p1.SetName("P1");
        pn.PlaceList.add(p1);

        DataTransfer p3 = new DataTransfer();
        p3.SetName("P3");
        p3.Value = new TransferOperation("localhost", "1081", "P1_S");
        pn.PlaceList.add(p3);

        DataFloat p4 = new DataFloat();
        p4.SetName("P4");
        pn.PlaceList.add(p4);

        DataFloat p5 = new DataFloat();
        p5.SetName("P5_C");
        pn.PlaceList.add(p5);

        DataFloat p6 = new DataFloat();
        p6.SetName("P6");
        pn.PlaceList.add(p6);

        // T1
        PetriTransition t1 = new PetriTransition(pn);
        t1.TransitionName = "T1";
        t1.InputPlaceName.add("P0");
        t1.InputPlaceName.add("P1");

        Condition T1Ct1 = new Condition(t1, "P0", TransitionCondition.NotNull);
        Condition T1Ct2 = new Condition(t1, "P1", TransitionCondition.NotNull);
        T1Ct1.SetNextCondition(LogicConnector.AND, T1Ct2);

        GuardMapping grdT1 = new GuardMapping();
        grdT1.condition = T1Ct1;

        grdT1.Activations.add(new Activation(t1, "P1", TransitionOperation.SendOverNetwork, "p3"));
        grdT1.Activations.add(new Activation(t1, "P0", TransitionOperation.Move, "P4"));
        t1.GuardMappingList.add(grdT1);

        t1.Delay = 0;
        pn.Transitions.add(t1);

        // T2
        PetriTransition t2 = new PetriTransition(pn);
        t2.TransitionName = "T2";
        t2.InputPlaceName.add("P4");
        t2.InputPlaceName.add("P5_C");

        Condition T2Ct1 = new Condition(t2, "P4", TransitionCondition.NotNull);
        Condition T2Ct2 = new Condition(t2, "P5_C", TransitionCondition.NotNull);
        T2Ct1.SetNextCondition(LogicConnector.AND, T2Ct2);

        GuardMapping grdT2 = new GuardMapping();
        grdT2.condition = T2Ct1;
        grdT2.Activations.add(new Activation(t2, "P5_C", TransitionOperation.Move, "P6"));
        grdT2.Activations.add(new Activation(t2, "P5_C", TransitionOperation.Move, "P0"));
        t2.GuardMappingList.add(grdT2);
        t2.Delay = 0;
        pn.Transitions.add(t2);

        pn.Delay = 3000;
        PetriNetWindow frame = new PetriNetWindow(false);
        frame.petriNet = pn;
        frame.setVisible(true);
    }
}
