/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package lk.gov.health.hiu.empanelment.jsfs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import lk.gov.health.hiu.empanelment.entities.Area;
import lk.gov.health.hiu.empanelment.entities.AreaType;
import lk.gov.health.hiu.empanelment.jsfs.util.JsfUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.IOUtils;

/**
 *
 * @author buddh
 */
@Named(value = "uploadController")
@SessionScoped
public class UploadController implements Serializable {

    @Inject
    AreaController areaController;

    private UploadedFile file;

    private String output;

    /**
     * Creates a new instance of UploadController
     */
    public UploadController() {
    }

    public String uploadCsvEc() {

        if (file == null) {
            JsfUtil.addErrorMessage("No file");
            return "";
        }
        try {

            Reader in = new InputStreamReader(new BOMInputStream(file.getInputStream()), "UTF-8");

            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
            output = "";
            for (CSVRecord record : records) {
                String districtEng = record.get(0);
                output += districtEng + "\n";
                String districtSin = record.get(1);
                output += districtSin + "\n";
                String districtTam = record.get(2);
                output += districtTam + "\n";
                String dsdSin = record.get(3);
                output += dsdSin + "\n";
                String dsdTam = record.get(4);
                output += dsdTam + "\n";
                String gnNum = record.get(5);
                output += gnNum + "\n";
                String gnSin = record.get(6);
                output += gnSin + "\n";
                String gnTam = record.get(7);
                output += gnTam + "\n";
                Area district = areaController.findArea(AreaType.District, null, null, districtEng, districtSin, districtTam, null, null);
                Area dsd = areaController.findArea(AreaType.DsdEc, district, null, null, dsdSin, dsdTam, null, null);
                Area gn = areaController.findArea(AreaType.GnEc, district, dsd, null, gnSin, gnTam, gnNum, null);
                output += gn.toString();
            }

        } catch (IOException ex) {
            System.out.println("ex = " + ex);
        }

        return "";
    }

    public String uploadCsvSgd() {

        if (file == null) {
            JsfUtil.addErrorMessage("No file");
            return "";
        }
        try {

            Reader in = new InputStreamReader(new BOMInputStream(file.getInputStream()), "UTF-8");

            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
            output = "";
            for (CSVRecord record : records) {
                String gnName = record.get(0);
                output += gnName + "\n";
                String gnNum = record.get(1);
                output += gnNum + "\n";
                String dsdName = record.get(2);
                output += dsdName + "\n";
                String districtEng = record.get(3);
                output += districtEng + "\n";
               
                Area district = areaController.findArea(AreaType.DistrictSgd, null, null, districtEng, null, null, null, null);
                Area dsd = areaController.findArea(AreaType.DsdSgd, district, null, dsdName, null, null, null, null);
                Area gn = areaController.findArea(AreaType.GnSgd, district, dsd, gnName, null, null, null, gnNum);
                output += gn.toString();
            }

        } catch (IOException ex) {
            System.out.println("ex = " + ex);
        }

        return "";
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

}
