/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mhwdi;

/**
 *
 * @author mhe TODO: implement owned/trash saving
 */
public class Dekoration {

    private int owned, toBeSmelted;
    private final String dekoName, skillName, skillDesc;
    private final int rowNum, skillMaxLvl, dekoLvl, dekoRarity, dekoRarityWeight;
    private final float dekoChanceMysterious, dekoChanceGlowing, dekoChanceWorn, dekoChanceWarped;
    private final float dekoChanceT1, dekoChanceT2, dekoChanceT3;

    public Dekoration(int rowNum, int owned, int toBeSmelted, String dekoName, String skillName,
            String skillDesc, int skillMaxLvl, int dekoLvl, int dekoRarity, int dekoRarityWeight,
            float dekoChanceMysterious, float dekoChanceGlowing, float dekoChanceWorn, float dekoChanceWarped,
            float dekoChanceT1, float dekoChanceT2, float dekoChanceT3) {
        this.rowNum = rowNum;
        this.owned = owned;
        this.toBeSmelted = toBeSmelted;
        this.dekoName = dekoName;
        this.skillName = skillName;
        this.skillDesc = skillDesc;
        this.skillMaxLvl = skillMaxLvl;
        this.dekoLvl = dekoLvl;
        this.dekoRarity = dekoRarity;
        this.dekoRarityWeight = dekoRarityWeight;
        this.dekoChanceMysterious = dekoChanceMysterious;
        this.dekoChanceGlowing = dekoChanceGlowing;
        this.dekoChanceWorn = dekoChanceWorn;
        this.dekoChanceWarped = dekoChanceWarped;
        this.dekoChanceT1 = dekoChanceT1;
        this.dekoChanceT2 = dekoChanceT2;
        this.dekoChanceT3 = dekoChanceT3;
    }

    public int getDekoRarityWeight() {
        return dekoRarityWeight;
    }

    public float getDekoChanceT1() {
        return dekoChanceT1;
    }

    public float getDekoChanceT2() {
        return dekoChanceT2;
    }

    public float getDekoChanceT3() {
        return dekoChanceT3;
    }

    public String printCSVData() {
        //this.rowNum = rowNum;
        StringBuilder sb = new StringBuilder();
        sb.append(this.owned).append(";");
        sb.append(this.skillMaxLvl - this.owned).append(";");
        sb.append(";"); //trash
        sb.append(this.dekoName).append(";");
        sb.append(this.skillName).append(";");
        sb.append(this.skillDesc).append(";");
        sb.append(this.skillMaxLvl).append(";");
        sb.append(this.dekoLvl).append(";");
        sb.append(this.dekoRarity).append(";");
        sb.append((this.dekoChanceMysterious == 0) ? "-;" : this.dekoChanceMysterious + "%;");
        sb.append((this.dekoChanceGlowing == 0) ? "-;" : this.dekoChanceGlowing + "%;");
        sb.append((this.dekoChanceWorn == 0) ? "-;" : this.dekoChanceWorn + "%;");
        sb.append((this.dekoChanceWarped == 0) ? "-" : this.dekoChanceWarped + "%");

        return sb.toString();
    }

    public void setOwned(int pOwned) {
        this.owned = pOwned;
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getOwned() {
        return owned;
    }

    public int getToBeSmelted() {
        return toBeSmelted;
    }

    public void setToBeSmelted(int pToBeSmelted) {
        this.toBeSmelted = pToBeSmelted;
    }

    public String getDekoName() {
        return dekoName;
    }

    public String getSkillName() {
        return skillName;
    }

    public String getSkillDesc() {
        return skillDesc;
    }

    public int getSkillMaxLvl() {
        return skillMaxLvl;
    }

    public int getDekoLvl() {
        return dekoLvl;
    }

    public int getDekoRarity() {
        return dekoRarity;
    }

    public float getDekoChanceMysterious() {
        return dekoChanceMysterious;
    }

    public float getDekoChanceGlowing() {
        return dekoChanceGlowing;
    }

    public float getDekoChanceWorn() {
        return dekoChanceWorn;
    }

    public float getDekoChanceWarped() {
        return dekoChanceWarped;
    }

    public Dekoration clone() {
        return new Dekoration(rowNum, new Integer(owned), toBeSmelted, dekoName, skillName, skillDesc, skillMaxLvl, dekoLvl, dekoRarity, dekoRarityWeight, dekoChanceMysterious, dekoChanceGlowing, dekoChanceWorn, dekoChanceWarped, dekoChanceT1, dekoChanceT2, dekoChanceT3);
    }
}
