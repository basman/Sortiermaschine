package sort;

/**
 * Created by Roman on 18.12.2015.
 */
public class DerChaot extends Sortiermethode {

    public DerChaot() {
        super();
        name = "Der Chaot";
    }

    @Override
    public void sortierSchritt() {
        super.sortierSchritt();
        
        // suche zwei zufällige Positionen aus
        int j1 = (int)(Math.random() * zahlenreihe.size());
        int j2 = (int)(Math.random() * zahlenreihe.size());

        if(j1 == j2) {
            if(j1 < zahlenreihe.size()-1)
                j1++;
            else
                j1--;
        }

        if(j1 > j2) {
            int y = j2;
            j2 = j1;
            j1 = y;
        }

        int z1 = zahlenreihe.get(j1);
        int z2 = zahlenreihe.get(j2);

        if(z1 > z2)
            vertausche(j1, j2);
    }
}
