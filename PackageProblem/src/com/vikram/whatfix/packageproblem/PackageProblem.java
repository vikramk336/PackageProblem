package com.vikram.whatfix.packageproblem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class PackageProblem {
	
	public static void main(String[] args) {

	PackageProblem pp = new PackageProblem();
  
        pp.solve("packageproblem.txt");
    
}

private final void solve(final String path) {

    final String comma = ",";

    final Pattern patColon = Pattern.compile(":");
    final Pattern patBraces = Pattern.compile("\\)\\(");
    final Pattern patComma = Pattern.compile(comma);

    final String oBrace = "(";
    final String cBrace = ")";

    final String empty = "";
    final String space = " ";
    final String dollar = "$";
    final String minus = "-";


    final int[] emptyIntAr = new int[0];

    try {
        int iSize;
        int packageMaxWeight;

        List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);

        for (String line : lines) {
            if (line.length() == 0) {
                continue;
            }
            line = line.replace(space, empty);
            String[] packline = patColon.split(line, 0);
            packageMaxWeight = Integer.valueOf(packline[0]);
            if (packageMaxWeight > 100) {
                continue;
            }
            String[] sItems = patBraces.split(packline[1], 0);

            int sLength = sItems.length;

            List<Item> items = new ArrayList<>(sLength);

            for (int i = 0; i < sLength; i++) {
                String sItem = sItems[i].replace(oBrace, empty);
                sItem = sItem.replace(cBrace, empty);
                String[] itemData = patComma.split(sItem, 0);

                final float weight = Float.valueOf(itemData[1]);

                if (weight <= packageMaxWeight) {
                    Item item = new Item();
                    item.index = Integer.valueOf(itemData[0]);
                    item.weight = weight;
                    item.cost = Integer.valueOf(itemData[2].replace(dollar, empty));
                    items.add(item);
                }
            }
            Collections.sort(items);

            iSize = items.size();
            int maxCost = 0;
            float maxWeight = packageMaxWeight;

            int[] cPack = emptyIntAr;
            int cPacked = 0;
            for (int i = 0; i < iSize; i++) {
                for (int j = i; j < iSize; j++) {

                    int currentCost = 0;
                    float currentWeight = 0;
                    int[] pack = new int[15];
                    int packed = 0;

                    final Item item1 = items.get(j);
                    if ((currentWeight + item1.weight) <= packageMaxWeight) {
                        pack[item1.index] = 1;
                        currentCost += item1.cost;
                        currentWeight += item1.weight;
                        packed++;
                    }

                    for (int k = i; k < iSize; k++) {
                        if (k != j) {
                            final Item item = items.get(k);
                            if ((currentWeight + item.weight) <= packageMaxWeight) {
                                pack[item.index] = 1;
                                currentCost += item.cost;
                                currentWeight += item.weight;
                                packed++;
                            }
                        }
                    }
                    if (packed > 0 && (currentCost > maxCost || (currentCost == maxCost && currentWeight < maxWeight))) {
                        maxCost = currentCost;
                        maxWeight = currentWeight;
                        cPack = pack;
                        cPacked = packed;
                    }
                }
            }
            if (cPacked > 0) {
                final StringBuilder sb = new StringBuilder(cPacked * 2);
                int j = 0;
                for (int i = 1; i < 15; i++) {
                    if (cPack[i] == 1) {
                        sb.append(i);
                        j++;
                        if (j < cPacked) {
                            sb.append(comma);
                        } else {
                            break;
                        }
                    }
                }
                System.out.println(sb.toString());
            } else {
                System.out.println(minus);
            }
        }
    } catch (IOException ex) {
        Logger.getLogger(PackageProblem.class.getName()).log(Level.SEVERE, null, ex);
    }
}

final class Item implements Comparable<Item> {
    int index;
    float weight;
    int cost;


    @Override
    public String toString() {
        return "Item{" +
                "index=" + index +
                ", weight=" + weight +
                ", cost=" + cost +
                '}';
    }

    @Override
    public int compareTo(final Item o) {
        if (cost == o.cost) {
            return weight > o.weight ? 1 : -1;
        } else {
            return cost < o.cost ? 1 : -1;
        }
    }
}
}
