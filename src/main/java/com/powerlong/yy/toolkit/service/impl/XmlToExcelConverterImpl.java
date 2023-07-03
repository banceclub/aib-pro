package com.powerlong.yy.toolkit.service.impl;

import com.powerlong.yy.toolkit.entity.InspectionResult;
import com.powerlong.yy.toolkit.service.XmlToExcelConverter;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * XmlToExcelConverterImpl
 *
 * @author: Eric(xuzhilu)
 * @createAt: 2020/12/7 11:55
 * @version: 1.0
 * @description:
 */
@Slf4j
@Service
public class XmlToExcelConverterImpl implements XmlToExcelConverter {

    @Override
    public List<InspectionResult> convert(String xmlPath) {
        List<InspectionResult> results = new ArrayList<>();
        try {
            List<File> xmlFiles = new ArrayList<>();
            Files.newDirectoryStream(Paths.get(xmlPath),
                    path -> path.toString().endsWith(".xml"))
                    .forEach(e ->
                            xmlFiles.add(new File(e.toUri()))
                    );

            for (File xmlFile : xmlFiles) {
                SAXReader reader = new SAXReader();
                Document document = reader.read(xmlFile);
                Element rootElm = document.getRootElement();
                if (!"problems".equals(rootElm.getName())) {
                    continue;
                }
                List<Element> elements = rootElm.elements("problem");
                for (Element element : elements) {
                    InspectionResult result = new InspectionResult();
                    String fileName = element.element("file").getText();
                    result.setFile(fileName.substring(fileName.lastIndexOf("$") + 1));
                    result.setLineNo(Integer.parseInt(element.element("line").getText()));
                    result.setDescription(element.element("description").getText());
                    result.setModule(element.element("module").getText());

                    Element entryPoint = element.element("entry_point");
                    result.setEntryPoinType(entryPoint.attributeValue("TYPE"));
                    result.setEntryPoint(entryPoint.attributeValue("FQNAME"));
                    Element problemClass = element.element("problem_class");
                    result.setSeverity(problemClass.attributeValue("severity"));
                    result.setProblem(problemClass.getText());
                    results.add(result);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return results.stream().sorted(Comparator.comparing(InspectionResult::getSeverity)).collect(Collectors.toList());
    }
}
