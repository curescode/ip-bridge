package com.curescode.bridge.swing;

import com.curescode.bridge.domain.IPAddress;
import com.curescode.bridge.io.ClientServer;
import com.curescode.bridge.swing.table.JServerTable;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;

import static com.curescode.bridge.system.SystemEnv.servers;

/**
 * main window
 * @author Cure
 * @date 2024/01/31 21:13
 */
public class ServerMainWindow extends JFrame {

    private ClientServer clientServer;

    JComboBox<String> protocol;
    private JTextField serverIPStr;
    private JSpinner serverPort;

    private JSpinner localPort;
    private JButton stopClientServer;
    private JButton startClientServer;

    private JServerTable infoTable;

    private void setLocalPort(){
        JLabel localPortLabel = new JLabel("本地端口");
        this.localPort = new JSpinner();
        this.localPort.setPreferredSize(new Dimension(75,25));
        this.localPort.setModel(new SpinnerNumberModel(8211,0,65535,1));
        this.add(localPortLabel);
        this.add(this.localPort);
    }

    private void setServerPort(){
        JLabel serverPortLabel = new JLabel("服务端口");
        this.serverPort =  new JSpinner();
        this.serverPort.setPreferredSize(new Dimension(75,25));
        this.serverPort.setModel(new SpinnerNumberModel(8212,0,65535,1));
        this.add(serverPortLabel);
        this.add(this.serverPort);
    }

    private void setServerIPStr(){
        this.serverIPStr = new JTextField();
        this.serverIPStr.setColumns(15);
        this.serverIPStr.setText("");
        this.add(new JLabel("服务IP/域名"));
        this.add(this.serverIPStr);
    }

    private void setStartServerButton(){
        this.startClientServer = new JButton(new FlatSVGIcon("com/curescode/bridge/icons/execute.svg"));
        this.stopClientServer = new JButton(new FlatSVGIcon("com/curescode/bridge/icons/suspend.svg"));
        JButton store = new JButton(new FlatSVGIcon("com/curescode/bridge/icons/FileViewFloppyDriveIcon.svg"));


        this.stopClientServer.setEnabled(false);
        this.startClientServer.addActionListener(this::startClientServerButtonHandler);
        this.stopClientServer.addActionListener(this::stopClientServerButtonHandler);
        store.addActionListener(this::storeClientServerButtonHandler);

        this.add(startClientServer);
        this.add(stopClientServer);

        this.add(store);

    }
    private void setProtocol(){
        String[] protocolStr= {"TCP","UDP"};
        this.protocol = new JComboBox<>(protocolStr);
        protocol.setSelectedIndex(1);
        protocol.setEnabled(false);
        this.add(protocol);
    }

    private void assemble(){
        // set window title
        this.setTitle("网桥服务器");

        LayoutManager layoutManager = new FlowLayout();

        this.setLayout(layoutManager);
        // set window location & size
        setFrameCenter();

        // init children panel
        setProtocol();
        setLocalPort();
        setServerIPStr();
        setServerPort();
        setStartServerButton();


        this.infoTable = getJServerTable();
        this.add(this.infoTable);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JServerTable getJServerTable(){
        String[] columnTitle = {"协议","本地端口","服务器IP/域名","远程端口","操作"};

        return new JServerTable(columnTitle,servers);
    }


    public ServerMainWindow(){
        // assembly interface
        assemble();
        // this canvas displays
        this.setVisible(true);
    }

    private void setFrameCenter(){
        int width = 720;
        int height = 576;

        // get user display size
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension dimension = t.getScreenSize();

        // set size & location adapt user display
        this.setSize(width, height);

        this.setLocation((int) ((dimension.getWidth() - width - 10)/2),(int) ((dimension.getHeight() - height -10)/2));

    }

    /**
     * listen store server parameters button
     * @param e button event
     */
    private void storeClientServerButtonHandler(ActionEvent e){
        // get parameters
        HashMap<String,String> inputMap = new HashMap<>();
        inputMap.put("protocol",protocol.getItemAt(protocol.getSelectedIndex()));
        inputMap.put("localPort", this.localPort.getValue().toString());
        inputMap.put("serverAddr",this.serverIPStr.getText());
        inputMap.put("serverPort", this.serverPort.getValue().toString());
        // append to list
        servers.add(inputMap);
        this.infoTable.addRow(inputMap);
    }

    /**
     * listen stop server button
     * @param e button event
     */
    private void stopClientServerButtonHandler(ActionEvent e){
        if (this.clientServer != null) this.clientServer.stopUDPBridge();
        this.startClientServer.setEnabled(true);
        this.stopClientServer.setEnabled(false);
    }

    /**
     * listen start server button
     * @param e button event
     */
    private void startClientServerButtonHandler(ActionEvent e){
        if (this.serverIPStr.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入IP/域名",
                    "警告", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (this.clientServer != null) this.clientServer.stopUDPBridge();
        this.clientServer = new ClientServer((Integer) this.localPort.getValue(),
                new IPAddress(this.serverIPStr.getText(),
                (Integer) this.serverPort.getValue()));

        try {
            // 启动服务
            this.clientServer.initUDP();
            this.startClientServer.setEnabled(false);
            this.stopClientServer.setEnabled(true);

        } catch (Exception ex){
            if ("Address already in use: bind".equals(ex.getMessage())){
                JOptionPane.showMessageDialog(null, "本地端口已被绑定，请更换端口",
                        "发生错误", JOptionPane.ERROR_MESSAGE);
            }else {
                // todo 换日志框架
                System.out.println(ex.getMessage());
            }
        }

    }
}
