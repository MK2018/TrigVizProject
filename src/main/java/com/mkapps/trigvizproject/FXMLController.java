package com.mkapps.trigvizproject;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

public class FXMLController implements Initializable {
    
    @FXML
    private Label outputText;
    @FXML
    private TextField inputText;
    
    @FXML
    private void doQuery(ActionEvent event) {
        //String input = "pi";
        //if (args.length > 0)
        //    input = args[0];
        
        // The WAEngine is a factory for creating WAQuery objects,
        // and it also used to perform those queries. You can set properties of
        // the WAEngine (such as the desired API output format types) that will
        // be inherited by all WAQuery objects created from it. Most applications
        // will only need to crete one WAEngine object, which is used throughout
        // the life of the application.
        WAEngine engine = new WAEngine();
        
        // These properties will be set in all the WAQuery objects created from this WAEngine.
        engine.setAppID("A6YK4P-2WRWT4AYGR");
        engine.addFormat("plaintext");

        // Create the query.
        WAQuery query = engine.createQuery();
        
        // Set properties of the query.
        query.setInput(inputText.getText());
        
        try {
            // For educational purposes, print out the URL we are about to send:
            System.out.println("Query URL:");
            System.out.println(engine.toURL(query));
            System.out.println("");
            
            // This sends the URL to the Wolfram|Alpha server, gets the XML result
            // and parses it into an object hierarchy held by the WAQueryResult object.
            WAQueryResult queryResult = engine.performQuery(query);

            if (queryResult.isError()) {
                System.out.println("Query error");
                System.out.println("  error code: " + queryResult.getErrorCode());
                System.out.println("  error message: " + queryResult.getErrorMessage());
            } else if (!queryResult.isSuccess()) {
                System.out.println("Query was not understood; no results available.");
            } else {
                // Got a result.
                outputText.setText("");
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError() && (pod.getTitle().equals("Result") || pod.getTitle().equals("Exact result")|| pod.getTitle().equals("Input")|| pod.getTitle().equals("Alternate forms")) ) {
                        outputText.setText(outputText.getText()+"\n"+pod.getTitle()+"\n------");
                        for (WASubpod subpod : pod.getSubpods()) {
                            for (Object element : subpod.getContents()) {
                                if (element instanceof WAPlainText) {
                                    outputText.setText(outputText.getText()+"\n"+((WAPlainText) element).getText().replace("\uF7D9", "=")+"\n---");
                                    System.out.println(((WAPlainText) element).getText().replace("\uF7D9", "="));
                                    //System.out.println(((WAPlainText) element).getText());
                                    //System.out.println("");
                                }
                            }
                        }
                    }
                }
                // We ignored many other types of Wolfram|Alpha output, such as warnings, assumptions, etc.
                // These can be obtained by methods of WAQueryResult or objects deeper in the hierarchy.
            }
        } catch (WAException e) {
            e.printStackTrace();
        }

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
}
