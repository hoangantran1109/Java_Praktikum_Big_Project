package MessageObjects;

import java.util.List;

/**
 * class Field
 *
 * Instances of Field are part of message "GameStarted" and are used to transfer
 * the fields and all of its relevant attributes of a chosen game board.
 */
public class Field {

    /**
     * Field()
     * @param type the distinct type every field has
     */
    public Field(String type) {
        this.type = type;
    }

    public String type;
    public int speed;
    public boolean isCrossing;
    public List<String> orientations;
    public String orientation;
    public List<Integer> registers;
    public int count;

    /**
     * equals()
     * Overrides the pre-defined equals method for type Field to use contains() and equals() type-based
     * @param o any instance of Field
     * @return true, if Object o is an instance of field and has the same type as the compared Field, false otherwise
     */
    @Override
    public boolean equals(Object o){
        if(o instanceof Field){
            Field toCompare = (Field) o;
            return this.type.equals(toCompare.type);
        }
        return false;
    }
    @Override
    public int hashCode() {
        return 1;
    }
}