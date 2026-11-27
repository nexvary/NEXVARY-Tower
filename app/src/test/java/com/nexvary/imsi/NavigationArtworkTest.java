package com.nexvary.imsi;

import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NavigationArtworkTest {
    private static final String ANDROID = "http://schemas.android.com/apk/res/android";

    @Test
    public void bottomNavigationUsesEqualSquareMulticolorVectors() throws Exception {
        for (String name : new String[]{"home", "cells", "history", "reports", "info"}) {
            Element icon = load(name);
            assertEquals(name, "24dp", icon.getAttributeNS(ANDROID, "width"));
            assertEquals(name, "24dp", icon.getAttributeNS(ANDROID, "height"));
            assertEquals(name, "24", icon.getAttributeNS(ANDROID, "viewportWidth"));
            assertEquals(name, "24", icon.getAttributeNS(ANDROID, "viewportHeight"));
            assertTrue(name, icon.getAttributeNS(ANDROID, "tint").isEmpty());

            Set<String> colors = new HashSet<>();
            NodeList paths = icon.getElementsByTagName("path");
            for (int i = 0; i < paths.getLength(); i++) {
                Element path = (Element) paths.item(i);
                for (String attribute : new String[]{"fillColor", "strokeColor"}) {
                    String color = path.getAttributeNS(ANDROID, attribute);
                    if (color.startsWith("#")) colors.add(color);
                }
            }
            assertTrue(name, colors.size() > 1);
        }
    }

    @Test
    public void reportsArtworkStaysInsideViewportWithClearance() throws Exception {
        NodeList paths = load("reports").getElementsByTagName("path");
        assertTrue(paths.getLength() > 0);
        Pattern coordinate = Pattern.compile("-?\\d+(?:\\.\\d+)?");
        for (int i = 0; i < paths.getLength(); i++) {
            String data = ((Element) paths.item(i)).getAttributeNS(ANDROID, "pathData");
            // Reports uses absolute move/line commands on a square viewport.
            assertTrue(data, data.matches("[MLZ0-9., -]+"));
            Matcher numbers = coordinate.matcher(data);
            assertTrue(data, numbers.find());
            do {
                double value = Double.parseDouble(numbers.group());
                assertTrue(data, value >= 1 && value <= 23);
            } while (numbers.find());
        }
    }

    private static Element load(String name) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Path path = Path.of("src", "main", "res", "drawable", "ic_nav_" + name + ".xml");
        return factory.newDocumentBuilder().parse(path.toFile()).getDocumentElement();
    }
}
