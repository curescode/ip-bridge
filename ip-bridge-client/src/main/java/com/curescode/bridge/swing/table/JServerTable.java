package com.curescode.bridge.swing.table;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 * the server table
 * @author Cure
 * @date 2024/02/03 16:32
 */
public class JServerTable extends JPanel {

    /**
     * the title
     */
    private final String[] title;

    public void addRow(HashMap<String,String> inputMap){
        if (inputMap.get("serverAddr").isEmpty()){
            this.add(new JServerRowPanel(inputMap.get("protocol"), Integer.parseInt(inputMap.get("localPort")),
                    "127.0.0.1", Integer.parseInt(inputMap.get("serverPort"))));
        }else {
            this.add(new JServerRowPanel(inputMap.get("protocol"), Integer.parseInt(inputMap.get("localPort")),
                    inputMap.get("serverAddr"), Integer.parseInt(inputMap.get("serverPort"))));
        }

        // repaint window
        this.updateUI();
    }


    public JServerTable(String[] columnTitle, List<HashMap<String,String>> rowsData){

        this.title = columnTitle;
        // set layout
        BoxLayout boxLayout = new BoxLayout(this,BoxLayout.Y_AXIS);

        // set title
        this.add(setTitlePanel());

        // set rows
        for (HashMap<String,String> rowData : rowsData) {
            JServerRowPanel rowPanel = new JServerRowPanel(rowData.get("protocol"), Integer.parseInt(rowData.get("localPort")),
                    rowData.get("serverAddr"), Integer.parseInt(rowData.get("serverPort")));
            this.add(rowPanel);
        }

        this.setSize(700,288);
        this.setLayout(boxLayout);
        this.setBackground(Color.WHITE);
    }


    private JPanel setTitlePanel(){
        JPanel titleRow = new JPanel();
        for(String x
                : this.title){
            JLabel title = new JLabel(x);
            title.setHorizontalAlignment(SwingConstants.CENTER);
            titleRow.add(title);
        }
        GridLayout a = new GridLayout(1,this.title.length);
        titleRow.setLayout(a);
        return titleRow;
    }

}
