import java.util.ArrayList;
import java.util.Arrays;

public class Temp {
    ArrayList<String> heroPool = new ArrayList<String>();
    ArrayList<String> itemPool;
    boolean roleToggle;

    public void setHeroPool(ArrayList<String> userHeroPool) {
        heroPool = userHeroPool;
    }

    public ArrayList<String> getHeroPool() {
        return heroPool;
    }

    public void setItemPool() {
        itemPool = new ArrayList<String>(Arrays.asList("Blink Dagger", "Ghost Scepter", "Mask of Madness",
                "Helm of the Dominator", "Moon Shard", "Hand of Midas", "Helm of the Overlord", "Mekansm",
                "Vladimir's Offering", "Pipe of Insight", "Boots of Bearing", "Spirit Vessel", "Urn of Shadows",
                "Medallion of Courage", "Drum of Endurance", "Holy Locket", "Wraith Pact", "Guardian Greaves",
                "Veil of Discord", "Force Staff", "Witch Blade", "Dagon", "Rod of Atos", "Aghanim's Scepter",
                "Octarine Core", "Revenant's Brooch", "Glimmer Cape", "Aether Lens", "Solar Crest",
                "Eul's Scepter of Divinity", "Orchid Malevolence", "Refresher Orb", "Scythe of Vyse", "Gleinir",
                "Wind Waker", "Hood of Defiance", "Blade Mail", "Crimson Guard", "Black King Bar", "Bloodstone",
                "Manta Style", "Heart of Tarrasque", "Vanguard", "Aeon Disk", "Eternal Shroud", "Lotus Orb",
                "Hurricane Pike", "Linken's Sphere", "Shiva's Guard", "Assault Cuirass", "Crystalys",
                "Armlet of Mordiggian", "Shadow Blade", "Battle Fury", "Nullifier", "Monkey King Bar", "Daedalus",
                "Divine Rapier", "Bloodthorn", "Meteor Hammer", "Skull Basher", "Desolator", "Ethereal Blade",
                "Butterfly", "Radiance", "Silver Edge", "Abyssal Blade", "Dragon Lance", "Sange", "Mage Slayer",
                "Echo Sabre", "Heaven's Halberd", "Sange and Yasha", "Satanic", "Mjollnir", "Overwhelming Blink",
                "Kaya", "Yasha", "Diffusal Blade", "Maelstrom", "Kaya and Sange", "Yasha and Kaya", "Eye of Skadi",
                "Arcane Blink", "Swift Blink","Falcon Blade", "Orb of Corrosion"));
    }

    public ArrayList<String> getItemPool(){
        return itemPool;
    }

    public void setRoleToggle() {
        roleToggle = false;
    }

    public boolean getRoleToggle() {
        return roleToggle;
    }

    public String pickHero() {
        int index = (int) (Math.random() * heroPool.size());
        return heroPool.get(index);
    }

    public String pickRole() {
        int roleNumber = 0;
        String role = "";
        if (roleToggle == true) {
            roleNumber = (int) (Math.random() * 5);
        }
        switch (roleNumber) {
            case 1:
                role = "Position 1 / Safe Lane";
            case 2:
                role = "Position 2 / Mid Lane";
            case 3:
                role = "Position 3 / Offlane";
            case 4:
                role = "Position 4 / Support";
            case 5:
                role = "Position 5 / Hard Support";
        }
        return role;
    }

    public String pickItem(int slots){
        String itemBuild = "";
        for (int i = 0; i < slots; i++){
            int index = (int)(Math.random() * itemPool.size());
            itemBuild += " " + itemPool.get(index);
        }
        return itemBuild;
    }
}