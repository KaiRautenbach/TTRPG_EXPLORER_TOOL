package za.co.wethincode;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.management.monitor.Monitor;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.file.Files;

public class ResponseBuilder {
    String input;
    String dataJson;
    private final JsonArray allMonsters;

    public ResponseBuilder(String input) throws IOException {
        this.dataJson = Files.readString(JsonLoader.DATA_FILE);
        this.allMonsters = JsonParser.parseString(dataJson).getAsJsonArray();
        this.input = input;
    }

    public String build() {
        if (input.equalsIgnoreCase("all")) {
            return buildTable(allMonsters);
        }

        JsonArray matches = new JsonArray();
        for (JsonElement entry : allMonsters) {
            JsonObject monster = entry.getAsJsonObject();
            if (input.equalsIgnoreCase(monster.get("name").getAsString())) {
                matches.add(monster);
            }
        }
        return buildTable(matches);
    }


    private String buildTable(JsonArray monsters) {
        // Step 1: measure the widest value in each column
        int nameWidth = "Name".length();
        int typeWidth = "Type".length();
        int hpWidth = "HP".length();
        StringBuilder sb = new StringBuilder();
        if (monsters.size() >1){
            for (JsonElement entry : monsters) {
                JsonObject m = entry.getAsJsonObject();
                nameWidth = Math.max(nameWidth, m.get("name").getAsString().length());
                typeWidth = Math.max(typeWidth, m.get("type").getAsString().length());
                hpWidth = Math.max(hpWidth, m.get("hit_points").getAsString().length());
            }

            // Step 2: build the format string using those measured widths
            String rowFormat = "| %-" + (nameWidth + 2) + "s| %-" + (typeWidth + 2) + "s| %-" + (hpWidth + 2) + "s| %n";

            // Step 3: print header + every row using that one format


            sb.append(String.format(rowFormat, "Name", "Type", "HP"));
            for (JsonElement entry : monsters) {
                JsonObject m = entry.getAsJsonObject();
                sb.append(String.format(rowFormat,
                        m.get("name").getAsString(),
                        m.get("type").getAsString(),
                        m.get("hit_points").getAsString()));
            }
            return sb.toString();
        }else if (monsters.size() == 1){
            JsonObject m = monsters.get(0).getAsJsonObject();

            int armourClassWidth = "AC".length();
            String armourClassValue = m.getAsJsonArray("armor_class")
                    .get(0).getAsJsonObject()
                    .get("value").getAsString();

            nameWidth = Math.max(nameWidth, m.get("name").getAsString().length());
            typeWidth = Math.max(typeWidth, m.get("type").getAsString().length());
            hpWidth = Math.max(hpWidth, m.get("hit_points").getAsString().length());
            armourClassWidth = Math.max(armourClassWidth, armourClassValue.length());

            int totalLength1 = nameWidth + typeWidth + hpWidth + armourClassWidth + 17;
            String divider1 = "-".repeat(totalLength1);
            String rowFormat1 = "| %-" + (nameWidth + 2) + "s| %-" + (typeWidth + 2) + "s| %-" + (hpWidth + 2) + "s| %-" + (armourClassWidth + 2) + "s| %n";

            sb.append(divider1).append("\n");
            sb.append(String.format(rowFormat1, "Name", "Type", "HP","AC"));
            sb.append(String.format(rowFormat1,
                    m.get("name").getAsString(),
                    m.get("type").getAsString(),
                    m.get("hit_points").getAsString(),
                    armourClassValue));
            sb.append(divider1).append("\n");
            sb.append("\n");

            int StrWidth = Math.max("STR".length(), String.valueOf(m.get("strength").getAsInt()).length());
            int DexWidth = Math.max("DEX".length(), String.valueOf(m.get("dexterity").getAsInt()).length());
            int ConWidth = Math.max("CON".length(), String.valueOf(m.get("constitution").getAsInt()).length());
            int IntWidth = Math.max("INT".length(), String.valueOf(m.get("intelligence").getAsInt()).length());
            int WisWidth = Math.max("WIS".length(), String.valueOf(m.get("wisdom").getAsInt()).length());
            int ChaWidth = Math.max("CHA".length(), String.valueOf(m.get("charisma").getAsInt()).length());

            int totalLength2 = StrWidth + DexWidth + ConWidth + IntWidth + WisWidth + ChaWidth + 25;
            String divider2 = "-".repeat(totalLength2);
            String rowFormat2 = "| %-" + (StrWidth + 2) + "s| %-" + (DexWidth + 2) + "s| %-" + (ConWidth + 2) + "s| %-" + (IntWidth + 2) + "s| %-" + (WisWidth + 2) + "s| %-" + (ChaWidth + 2) + "s| %n";

            sb.append(divider2).append("\n");
            sb.append("| STAT BLOCK").append(" ".repeat(totalLength2 - 13)).append("|\n");
            sb.append(divider2).append("\n");
            sb.append(String.format(rowFormat2, "STR","DEX","CON","INT","WIS","CHA"));

            sb.append(String.format(rowFormat2,
                    m.get("strength").getAsString(),
                    m.get("dexterity").getAsString(),
                    m.get("constitution").getAsString(),
                    m.get("intelligence").getAsString(),
                    m.get("wisdom").getAsString(),
                    m.get("charisma").getAsString()));
            sb.append(divider2).append("\n");


            return sb.toString();

        }else{
            return "This creature does not exits or input was invalid!!!";
        }
    }

}
