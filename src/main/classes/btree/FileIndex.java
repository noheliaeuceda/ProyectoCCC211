
package main.classes.btree;


public class FileIndex{
    private String Id;
    private int rrn;

    public FileIndex(String Id, int rrn) {
        this.Id = Id;
        this.rrn = rrn;
    }

    FileIndex() {
        
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public int getRrn() {
        return rrn;
    }

    public void setRrn(int rrn) {

        this.rrn = rrn;
    }

    @Override
    public String toString() {
        return "indice{" + "Id=" + Id + ", rrn=" + rrn + '}';
    }

    
}
