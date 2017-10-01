/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartimagecrop;

import java.util.LinkedList;

/**
 *
 * @author sande
 */
public class SizeResult {
    private int minWidth;
    private int minHeight;
    
    private final LinkedList<Integer[]> imagePaddingCollection;
    
    public SizeResult(){
        imagePaddingCollection = new LinkedList<>();
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public Integer[] getPadding(int index) {
        return imagePaddingCollection.get(index);
    }
    
    public void addPadding(Integer[] value){
        imagePaddingCollection.add(value);
    }
    
}
