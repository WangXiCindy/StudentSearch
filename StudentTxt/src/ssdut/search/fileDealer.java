package ssdut.search;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
 
/**
 * @description: 把文件读到list当中
 * @author: zhangyu
 */
public class fileDealer {
    public static List<String> getFileToList(String filePath) {
        List<String> readFileList = null;
        try {
            readFileList = FileUtils.readLines(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        return readFileList;
    }
    
}

