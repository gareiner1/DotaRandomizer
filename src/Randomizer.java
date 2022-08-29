import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Randomizer {
    private final Map<String, ImageIcon> heroIconMap;
    private final Map<String, ImageIcon> itemIconMap;
    private final String[] dotaHeroes = new String[]{"Abaddon", "Alchemist", "Ancient Apparition",
            "Anti-Mage", "Arc Warden", "Axe", "Bane", "Batrider", "Beastmaster", "Bloodseeker", "Bounty Hunter", "Brewmaster",
            "Bristleback", "Broodmother", "Centaur Warrunner", "Chaos Knight", "Chen", "Clinkz", "Clockwerk", "Crystal Maiden",
            "Dark Seer", "Dark Willow", "Dawnbreaker", "Dazzle", "Death Prophet", "Disruptor", "Doom", "Dragon Knight",
            "Drow Ranger", "Earth Spirit", "Earthshaker", "Elder Titan", "Ember Spirit", "Enchantress", "Enigma",
            "Faceless Void", "Gyrocopter", "Grimstroke", "Hoodwink", "Huskar", "Invoker", "Io", "Jakiro", "Juggernaut",
            "Keeper of the Light", "Kunkka", "Legion Commander", "Leshrac", "Lich", "Lifestealer", "Lina", "Lion", "Lone Druid",
            "Luna", "Lycan", "Magnus", "Marci", "Mars", "Medusa", "Meepo", "Mirana", "Monkey King", "Morphling", "Naga Siren",
            "Nature's Prophet", "Necrophos", "Night Stalker", "Nyx Assassin", "Ogre Magi", "Omniknight", "Oracle",
            "Outworld Destroyer", "Pangolier", "Phantom Assassin", "Phantom Lancer", "Phoenix", "Primal Beast", "Puck", "Pudge",
            "Pugna", "Queen of Pain", "Razor", "Riki", "Rubick", "Sand King", "Shadow Demon", "Shadow Fiend", "Shadow Shaman",
            "Silencer", "Skywrath Mage", "Slardar", "Slark", "Snapfire", "Sniper", "Spectre", "Spirit Breaker", "Storm Spirit",
            "Sven", "Techies", "Templar Assassin", "Terrorblade", "Tidehunter", "Timbersaw", "Tinker", "Tiny", "Treant Protector",
            "Troll Warlord", "Tusk", "Underlord", "Undying", "Ursa", "Vengeful Spirit", "Venomancer", "Viper", "Visage",
            "Void Spirit", "Warlock", "Weaver", "Windranger", "Winter Wyvern", "Witch Doctor", "Wraith King", "Zeus"};
    private final ArrayList<String> dotaItems = new ArrayList<String>(Arrays.asList("Blink Dagger", "Ghost Scepter", "Mask of Madness",
            "Helm of the Dominator", "Moon Shard", "Hand of Midas", "Helm of the Overlord", "Mekansm",
            "Vladimir's Offering", "Pipe of Insight", "Boots of Bearing", "Spirit Vessel",
            "Medallion of Courage", "Drum of Endurance", "Holy Locket", "Wraith Pact", "Guardian Greaves",
            "Veil of Discord", "Force Staff", "Witch Blade", "Dagon", "Rod of Atos", "Aghanim's Scepter",
            "Octarine Core", "Revenant's Brooch", "Glimmer Cape", "Aether Lens", "Solar Crest",
            "Eul's Scepter of Divinity", "Orchid Malevolence", "Refresher Orb", "Scythe of Vyse", "Gleipnir",
            "Wind Waker", "Hood of Defiance", "Blade Mail", "Crimson Guard", "Black King Bar", "Bloodstone",
            "Manta Style", "Heart of Tarrasque", "Vanguard", "Aeon Disk", "Eternal Shroud", "Lotus Orb",
            "Hurricane Pike", "Linken's Sphere", "Shiva's Guard", "Assault Cuirass", "Crystalys",
            "Armlet of Mordiggian", "Shadow Blade", "Battle Fury", "Nullifier", "Monkey King Bar", "Daedalus",
            "Divine Rapier", "Bloodthorn", "Meteor Hammer", "Skull Basher", "Desolator", "Ethereal Blade",
            "Butterfly", "Radiance", "Silver Edge", "Abyssal Blade", "Dragon Lance", "Sange", "Mage Slayer",
            "Echo Sabre", "Heaven's Halberd", "Sange and Yasha", "Satanic", "Mjollnir", "Overwhelming Blink",
            "Kaya", "Yasha", "Diffusal Blade", "Maelstrom", "Kaya and Sange", "Yasha and Kaya", "Eye of Skadi",
            "Arcane Blink", "Swift Blink","Falcon Blade", "Orb of Corrosion"));
    private final String[] positionChoices = {"Random", "None", "Position 1 (Carry)", "Position 2 (Mid)", "Position 3 (Offlane)",
            "Position 4 (Support)", "Position 5 (Hard Support)"};
    private final String[] slotChoices = {"Random", "None", "1", "2", "3", "4", "5", "6"};
    private final ArrayList<String> userHeroPoolList = new ArrayList<String>();
    private JPanel resultPanel = new JPanel();
    private String[] userHeroPoolArray = new String[123];
    private boolean ifSaved = false;
    private final DefaultListModel<String> userHeroPoolModel = new DefaultListModel<String>();
    private final DefaultListModel<String> defaultHeroModel = new DefaultListModel<String>();
    private final DefaultListModel<String> userHeroPoolModelSaved = new DefaultListModel<String>();

    public Randomizer() throws IOException {
        File userHeroPool = new File("hero_pool.txt");
        Scanner sc = new Scanner(userHeroPool);
        while (sc.hasNext()) {
            String hero = sc.nextLine();
            userHeroPoolModel.addElement(hero);
            userHeroPoolModelSaved.addElement(hero);
            userHeroPoolList.add(hero);
        }
        Collections.sort(userHeroPoolList);
        sc.close();
        setHeroModels();

        heroIconMap = createHeroIconMap(userHeroPoolList);
        itemIconMap = createItemIconMap(dotaItems);
        JList heroPoolList = new JList<>(userHeroPoolList.toArray());
        heroPoolList.setCellRenderer(new HeroListRenderer());
        JScrollPane leftScrollPane = new JScrollPane(heroPoolList);
        leftScrollPane.setPreferredSize(new Dimension(200, 400));

        JLabel roleChoice = new JLabel("Role:");
        JComboBox<String> roleChoiceBox = new JComboBox<String>(positionChoices);
        JLabel itemBuildChoice = new JLabel("Item Build Slots:");
        JComboBox<String> itemBuildChoiceBox = new JComboBox<String>(slotChoices);

        JFrame frame = new JFrame("Dota 2 Randomizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        Dimension scrollPaneSize = new Dimension(200, 400);

        JPanel upperPanel = new JPanel();
        JLabel heroPoolLabel = new JLabel("Hero Pool: (" + userHeroPoolList.size() + " Heroes)");
        leftScrollPane.setPreferredSize(scrollPaneSize);
        upperPanel.setLayout(new GridBagLayout());
        GridBagConstraints leftScrollPaneConstraints = new GridBagConstraints();
        leftScrollPaneConstraints.gridx = 0;
        leftScrollPaneConstraints.gridy = 0;
        leftScrollPaneConstraints.fill = GridBagConstraints.VERTICAL;
        leftScrollPaneConstraints.anchor = GridBagConstraints.NORTHWEST;
        leftScrollPaneConstraints.weightx = 0.5;
        leftScrollPaneConstraints.weighty = 0.5;
        leftScrollPaneConstraints.ipadx = 180;
        upperPanel.add(leftScrollPane, leftScrollPaneConstraints);

        GridBagConstraints roleAndItemConstraints = new GridBagConstraints();
        GridBagConstraints rolePanelConstraints = new GridBagConstraints();
        GridBagConstraints itemBuildPanelConstraints = new GridBagConstraints();
        roleAndItemConstraints.gridx = 1;
        roleAndItemConstraints.gridy = 0;
        roleAndItemConstraints.fill = GridBagConstraints.VERTICAL;
        roleAndItemConstraints.anchor = GridBagConstraints.NORTHWEST;
        roleAndItemConstraints.insets = new Insets(0, 0, 0, 35);
        rolePanelConstraints.gridx = 0;
        rolePanelConstraints.gridy = 0;
        rolePanelConstraints.fill = GridBagConstraints.VERTICAL;
        rolePanelConstraints.anchor = GridBagConstraints.NORTHWEST;
        itemBuildPanelConstraints.gridx = 0;
        itemBuildPanelConstraints.gridy = 1;
        itemBuildPanelConstraints.fill = GridBagConstraints.VERTICAL;
        itemBuildPanelConstraints.anchor = GridBagConstraints.WEST;
        JPanel roleAndItemPanel = new JPanel();
        roleAndItemPanel.setLayout(new GridBagLayout());
        JPanel rolePanel = new JPanel();
        JPanel itemBuildPanel = new JPanel();
        rolePanel.add(roleChoice);
        rolePanel.add(roleChoiceBox);
        itemBuildPanel.add(itemBuildChoice);
        itemBuildPanel.add(itemBuildChoiceBox);
        roleAndItemPanel.add(rolePanel, rolePanelConstraints);
        roleAndItemPanel.add(itemBuildPanel, itemBuildPanelConstraints);

        resultPanel.setBorder(BorderFactory.createTitledBorder("Result:"));
        resultPanel.setOpaque(true);
        resultPanel.setBackground(Color.WHITE);
        GridBagConstraints resultPanelConstraints = new GridBagConstraints();
        resultPanelConstraints.gridx = 0;
        resultPanelConstraints.gridy = 2;
        resultPanelConstraints.fill = GridBagConstraints.VERTICAL;
        resultPanelConstraints.ipadx = 200;
        resultPanelConstraints.weightx = 0.5;
        resultPanelConstraints.weighty = 0.5;
        resultPanelConstraints.insets = new Insets(90, 0 , 0, 0);
        resultPanelConstraints.anchor = GridBagConstraints.CENTER;
        roleAndItemPanel.add(resultPanel, resultPanelConstraints);
        upperPanel.add(roleAndItemPanel, roleAndItemConstraints);

        JButton editHeroPoolButton = new JButton("Edit Hero Pool");
        editHeroPoolButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    createHeroPoolFrame();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JPanel panelSouth = new JPanel();
        JButton randomizeButton = new JButton("Randomize");
        randomizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String roleResult = "";
                ArrayList<String> itemBuildResult = new ArrayList<String>();
                String heroResult = "";
                String roleSelection = roleChoiceBox.getSelectedItem().toString();
                String itemBuildSelection = itemBuildChoiceBox.getSelectedItem().toString();

                if (roleSelection.equals("Random")){
                    int randomNum = ThreadLocalRandom.current().nextInt(2, 6 + 1);
                    roleResult = roleChoiceBox.getItemAt(randomNum);
                }
                else{
                    roleResult = roleSelection;
                }

                if (itemBuildSelection.equals("Random")){
                    int randomNum = ThreadLocalRandom.current().nextInt(2, 6 + 1);
                    for (int i = 0; i < randomNum; i++){
                        Random r = new Random();
                        int rnd = r.nextInt(dotaItems.size());
                        if (itemBuildResult.contains(dotaItems.get(rnd))){
                            rnd = r.nextInt(dotaItems.size());
                        }
                        else {
                            itemBuildResult.add(dotaItems.get(rnd));
                        }
                    }
                }
                else if (!itemBuildSelection.equals("None")){
                    for (int i = 0; i < Integer.parseInt(itemBuildSelection); i++){
                        Random r = new Random();
                        int rnd = r.nextInt(dotaItems.size());
                        if (itemBuildResult.contains(dotaItems.get(rnd))){
                            rnd = r.nextInt(dotaItems.size());
                        }
                        else {
                            itemBuildResult.add(dotaItems.get(rnd));
                        }
                    }
                }

                Random r = new Random();
                int rnd = r.nextInt(userHeroPoolList.size());
                heroResult = userHeroPoolList.get(rnd);

                resultPanel.removeAll();
                JPanel tempResultPanel = new JPanel();
                tempResultPanel.setLayout(new GridBagLayout());
                GridBagConstraints heroResultConstraints = new GridBagConstraints();
                heroResultConstraints.gridx = 0;
                heroResultConstraints.gridy = 0;
                GridBagConstraints roleResultConstraints = new GridBagConstraints();
                roleResultConstraints.gridx = 0;
                roleResultConstraints.gridy = 1;
                GridBagConstraints itemBuildResultConstraints = new GridBagConstraints();
                itemBuildResultConstraints.gridx = 0;
                itemBuildResultConstraints.gridy = 2;
                JLabel heroResultLabel = new JLabel(heroResult);
                heroResultLabel.setIcon(heroIconMap.get(heroResult));
                heroResultLabel.setHorizontalTextPosition(JLabel.RIGHT);
                JLabel roleResultLabel = new JLabel(roleResult);

                JList itemBuildResultList = new JList<>(itemBuildResult.toArray());
                itemBuildResultList.setCellRenderer(new ItemListRenderer());

                tempResultPanel.add(heroResultLabel, heroResultConstraints);
                tempResultPanel.add(roleResultLabel, roleResultConstraints);
                tempResultPanel.add(itemBuildResultList, itemBuildResultConstraints);
                resultPanel.add(tempResultPanel);
                resultPanel.validate();
                resultPanel.repaint();
            }
        });

        panelSouth.add(randomizeButton);
        panelSouth.add(editHeroPoolButton);
        frame.getContentPane().add(BorderLayout.NORTH, heroPoolLabel);
        frame.add(upperPanel);
        frame.getContentPane().add(BorderLayout.SOUTH, panelSouth);
        frame.setVisible(true);
    }
    public class HeroListRenderer extends DefaultListCellRenderer{
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setIcon(heroIconMap.get((String) value));
            label.setHorizontalTextPosition(JLabel.RIGHT);
            return label;
        }
    }
    public class ItemListRenderer extends DefaultListCellRenderer{
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setIcon(itemIconMap.get((String) value));
            label.setHorizontalTextPosition(JLabel.RIGHT);
            return label;
        }
    }
    private void setHeroModels(){
        ArrayList<String> allHeroesList = new ArrayList<String>(Arrays.asList(dotaHeroes));
        for (String s : allHeroesList){
            defaultHeroModel.addElement(s);
        }

        for (int i = 0; i < userHeroPoolModel.size(); i++){
            defaultHeroModel.removeElement(userHeroPoolModel.get(i));
        }
    }
    public Map<String, ImageIcon> createHeroIconMap(ArrayList<String> list){
        Map<String, ImageIcon> map = new HashMap<>();
        for (String s : list){
            ImageIcon icon = new ImageIcon("D:/Code/Java Projects/Dota_Randomizer/images/" + s + "_icon.png");
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(51, 29, Image.SCALE_SMOOTH);
            icon = new ImageIcon(newImg);
            map.put(s, icon);
        }
        return map;
    }
    public Map<String, ImageIcon> createItemIconMap(ArrayList<String> list){
        Map<String, ImageIcon> map = new HashMap<>();
        for (String s : list){
            ImageIcon icon = new ImageIcon("D:/Code/Java Projects/Dota_Randomizer/images/" + s + "_icon.png");
            Image img = icon.getImage();
            Image newImg = img.getScaledInstance(41, 27, Image.SCALE_SMOOTH);
            icon = new ImageIcon(newImg);
            map.put(s, icon);
        }
        return map;
    }
    private void createHeroPoolFrame() throws Exception {
        JFrame frame = new JFrame("Edit Hero Pool");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 700);
        Dimension scrollPaneSize = new Dimension(200, 500);

        JLabel dotaHeroesLabel = new JLabel("Dota 2 Heroes");
        JList defaultDotaHeroes = new JList(defaultHeroModel);
        JScrollPane leftScrollPane = new JScrollPane();
        leftScrollPane.setViewportView(defaultDotaHeroes);
        defaultDotaHeroes.setLayoutOrientation(JList.VERTICAL);
        leftScrollPane.setPreferredSize(scrollPaneSize);

        JLabel userHeroesLabel = new JLabel("Your Heroes");
        JList userDotaHeroes = new JList<>(userHeroPoolModel);
        JScrollPane rightScrollPane = new JScrollPane();
        rightScrollPane.setViewportView(userDotaHeroes);
        userDotaHeroes.setLayoutOrientation(JList.VERTICAL);
        rightScrollPane.setPreferredSize(scrollPaneSize);

        defaultDotaHeroes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent m) {
                if (m.getClickCount() == 2) {
                    int index = defaultDotaHeroes.locationToIndex(m.getPoint());
                    if (index >= 0) {
                        Object heroRemoved = defaultDotaHeroes.getModel().getElementAt(index);
                        userHeroPoolModel.addElement(heroRemoved.toString());
                        userDotaHeroes.setModel(userHeroPoolModel);
                        defaultHeroModel.removeElement(heroRemoved);
                    }
                    defaultDotaHeroes.setModel(defaultHeroModel);
                }
            }
        });

        JPanel panelWest = new JPanel();
        panelWest.add(leftScrollPane);

        JPanel panelEast = new JPanel();
        panelEast.add(rightScrollPane);

        JPanel panelSouth = new JPanel();
        JButton addAllHeroesButton = new JButton("All Heroes");
        addAllHeroesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < defaultHeroModel.size(); i++) {
                    Object heroRemoved = defaultDotaHeroes.getModel().getElementAt(i);
                    userHeroPoolModel.addElement(heroRemoved.toString());
                }
                userDotaHeroes.setModel(userHeroPoolModel);
                defaultHeroModel.clear();
                defaultDotaHeroes.setModel(defaultHeroModel);
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                defaultHeroModel.clear();
                userHeroPoolModel.clear();
                ArrayList<String> allHeroesList = new ArrayList<String>(Arrays.asList(dotaHeroes));
                for (String s : allHeroesList){
                    defaultHeroModel.addElement(s);
                }
                userDotaHeroes.setModel(userHeroPoolModel);
                defaultDotaHeroes.setModel(defaultHeroModel);
            }
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter("hero_pool.txt"));
                    ArrayList userHeroPoolListTemp = new ArrayList<>(Arrays.asList(userHeroPoolArray));
                    for (int i = 0; i < userHeroPoolModel.size(); i++){
                        if (userHeroPoolModel.get(i) == null){
                            break;
                        }
                        else{
                            bw.write(userHeroPoolModel.get(i));
                            bw.newLine();
                        }
                    }
                    bw.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                ifSaved = true;
            }
        });
        panelSouth.add(addAllHeroesButton);
        panelSouth.add(resetButton);
        panelSouth.add(saveButton);

        frame.getContentPane().add(BorderLayout.NORTH, dotaHeroesLabel);
        frame.getContentPane().add(BorderLayout.WEST, panelWest);
        frame.getContentPane().add(BorderLayout.EAST, panelEast);
        frame.getContentPane().add(BorderLayout.SOUTH, panelSouth);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (ifSaved == true){
                    File userHeroPool = new File("hero_pool.txt");
                    Scanner sc = null;
                    userHeroPoolModelSaved.clear();
                    try {
                        sc = new Scanner(userHeroPool);
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    while (sc.hasNext()) {
                        userHeroPoolModelSaved.addElement(sc.nextLine());
                    }
                    sc.close();
                }
                ifSaved = false;
            }
        });
    }
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Randomizer();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
