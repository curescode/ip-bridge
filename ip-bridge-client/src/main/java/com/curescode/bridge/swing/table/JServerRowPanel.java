package com.curescode.bridge.swing.table;

import com.curescode.bridge.io.ClientServer;
import com.curescode.bridge.domain.IPAddress;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

/**
 * the row for JServerTable
 * @author Cure
 * @date 2024/02/03 19:13
 */
public class JServerRowPanel extends JPanel {

    private final String protocol;

    private final int localPort;

    private final String IPStr;

    private final int serverPort;

    private ClientServer clientServer;


    public JServerRowPanel(String protocol,int localPort,String IPStr,int serverPort){

        this.protocol = protocol;
        this.localPort = localPort;
        this.IPStr = IPStr;
        this.serverPort = serverPort;

        setInfoPanels();

        this.add(getButtonJPanel());

        this.setLayout(new GridLayout(1,5));

    }
    private void setInfoPanels(){
        // create info label
        JLabel protocolJLabel = new JLabel(protocol);
        protocolJLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel localPortJLabel = new JLabel(String.valueOf(localPort));
        localPortJLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel ipStrJLabel = new JLabel(IPStr);
        ipStrJLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel serverPortJLabel = new JLabel(String.valueOf(serverPort));
        serverPortJLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // add the labels to panel
        this.add(protocolJLabel);
        this.add(localPortJLabel);
        this.add(ipStrJLabel);
        this.add(serverPortJLabel);
    }

    /**
     * get button panel
     * @return button panel
     */
    private JPanel getButtonJPanel(){

        final JButton startClient = new JButton();
        startClient.setIcon(new FlatSVGIcon("com/curescode/bridge/icons/execute.svg"));

        final JButton stopClient = new JButton();
        stopClient.setIcon(new FlatSVGIcon("com/curescode/bridge/icons/suspend.svg"));
        stopClient.setEnabled(false);

        final JButton deleteClient = new JButton();
        deleteClient.setIcon(new FlatSVGIcon("com/curescode/bridge/icons/delete.svg"));

        startClient.addActionListener(e -> {
            // check parameters
            if (this.IPStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "请输入域名/IP",
                        "警告", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (this.clientServer != null) this.clientServer.stopUDPBridge();
            this.clientServer = new ClientServer(this.localPort, new IPAddress(this.IPStr, this.serverPort));

            try {
                // start server
                if ("TCP".equals(protocol)) {
                    this.clientServer.initTCP();
                }else {
                    System.out.println("init udp servers");
                    this.clientServer.initUDP();
                }

                // change icon status
                startClient.setEnabled(false);
                stopClient.setEnabled(true);
            } catch (Exception ex){
                if ("Address already in use: bind".equals(ex.getMessage())){
                    JOptionPane.showMessageDialog(null, "本地端口已被绑定，请更换端口",
                            "发生错误", JOptionPane.ERROR_MESSAGE);
                }else {
                    // todo 换日志框架
                    System.out.println(ex.getMessage());
                }
            }
        });
        stopClient.addActionListener(e -> {
            // stop server
            if (this.clientServer != null) this.clientServer.stopUDPBridge();

            stopClient.setEnabled(false);
            startClient.setEnabled(true);
        });
        deleteClient.addActionListener(e -> {
            // stop server
            if (this.clientServer != null) this.clientServer.stopUDPBridge();

            //delete this panel，repaint window
            JPanel parent = (JPanel) this.getParent();
            parent.remove(this);
            parent.updateUI();
        });

        JPanel clickJPanel = new JPanel();
        clickJPanel.add(startClient);
        clickJPanel.add(stopClient);
        clickJPanel.add(deleteClient);

        return clickJPanel;
    }

}