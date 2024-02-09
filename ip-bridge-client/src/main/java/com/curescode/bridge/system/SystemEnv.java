package com.curescode.bridge.system;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.curescode.bridge.utils.ObjectUtils.objConvertList;
import static com.curescode.bridge.utils.ObjectUtils.objectCoverMap;


/**
 * @author Cure
 * @date 2024/02/05 12:45
 */
public class SystemEnv {
    public static List<HashMap<String,String>> servers = new ArrayList<>();

    private static Map<String,Object> config;

    public void init(){
        // 从文件读取
        config = getConfigFromFile();

        if (config != null){
            // if the config is not null

            // extract proxies
            Object o = config.get("proxies");
            List<Object> proxies = objConvertList(o, Object.class);
            for(Object x
                : proxies){
                servers.add(objectCoverMap(x));
            }


        }else {
            // if the config is null,use default config
            // todo create config file
        }
    }

    /**
     * get config from file
     * @return config map
     */
    public Map<String,Object> getConfigFromFile(){
        try (FileInputStream configInputStream = new FileInputStream(getConfigPath())){
            // if there is a configuration file, return data
            return new Yaml().load(configInputStream);
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        // if there is no configuration file, return null
        return null;
    }

    /**
     * get config file absolute path string
     * @return config file absolute path string
     */
    private String getConfigPath(){
        String userDir = System.getProperty("user.dir");
        File file = new File(userDir);
        return file.getParentFile().getAbsolutePath() + "/config/ip-bridge.yml";
    }


    /**
     * get home dir
     * @return home dir file Object
     */
    private File findDefaultHomeDir() {
        String userDir = System.getProperty("user.dir");
        return new File(!userDir.isEmpty() ? userDir : ".");
    }

}
