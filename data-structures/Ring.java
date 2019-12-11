import java.util.NoSuchElementException;

public class Ring<T> {

    final T[] data; // Speicherbereich der Elemente; capacity nicht veränderbar
    int size;       // Länge des Rings
    int pivot;      // Aktuelles Element

    /* Workaround, um ein generisches Array zu erzeugen deren Elementypen die Typvariable T haben */
    @SuppressWarnings("unchecked")
    Ring(int cap) {
        if (cap < 0) throw new IllegalArgumentException("Illegale Capacity: " + cap);

        this.data = (T[]) new Object[cap];
        this.pivot = -1;    // Setze das aktuelle Element auf -1 für leere Ringe
    }

    int size() {
        return this.size;
    }

    int capacity() {
        return this.data.length;
    }

    void add(T t) {
        if (this.size() == this.capacity()) throw new IllegalArgumentException("Illegale Operation: Ring ist voll");

        // Falls der Ring leer ist, füge t als ersten Wert in den Ring ein und
        // erhöhe den Index des aktuellen Elements
        if (this.pivot == -1) {
            this.data[0] = t;
            this.pivot++;
        }
        // Falls das aktuelle Element am linken Rand des Speicherarrays ist,
        // füge t neben das letzte Element ein
        else if (this.pivot == 0) {
            this.data[this.size()] = t;
        }
        // Verschiebe alles nach rechts, beginnend mit dem aktuellen Element, sodass
        // die entstandene Lücke mit dem Wert t befüllt werden kann
        else {
            System.arraycopy(this.data, this.pivot, this.data, this.pivot + 1, this.size() - this.pivot);
            this.data[this.pivot] = t;
            this.pivot++;
        }
        this.size++;
    }

    void back() {
        if (this.size == 0) throw new NoSuchElementException("Illegale Operation: Ring ist leer");

        pivot = (this.pivot + this.size - 1) % this.size;
    }

    void remove() {
        if (this.size == 0) throw new NoSuchElementException("Illegale Operation: Ring ist leer");

        int numMoved = this.size - this.pivot - 1;

        // Lösche das aktuelle Element, indem jedes Element rechts davon (im Speicherbereich)
        // um einen Platz nach links verschoben wird
        if (numMoved > 0) {
            System.arraycopy(this.data, this.pivot + 1, this.data, this.pivot, numMoved);
        }
        // Wenn das aktuelle Element am rechten Rand des Speicherbereichs ist, setze den Pointer
        // auf das Element links daneben (der Speicher muss nicht manuell freigegeben werden)
        else {
            this.pivot = (this.pivot + 1) % this.size;
        }
        this.size--;
    }

    void set(T t) {
        if (this.size == 0) throw new NoSuchElementException("Illegale Operation: Ring ist leer");

        // Ersetze das akt. El. durch t
        this.data[this.pivot] = t;

        // Mache das Element rechts davon zum neuen akt. El.
        this.pivot = (this.pivot + 1) % this.size;
    }

    /* Gibt einen String der Form: Ring(Element_1, Element_2, etc.) aus */
    @Override
    public String toString() {
        String output = "Ring(";
        if (this.size() > 0) {
            // Beginnend mit dem aktuellen Element, erhöhe den Index i bis
            // wir wieder am aktuellen Element angekommen sind
            output += this.data[this.pivot];
            int i = (this.pivot + 1) % this.size();
            while (this.size() != 1 && i != this.pivot) {
                output += ", " + this.data[i];
                // Falls der Pointer am rechten Rand des Speicherarrays
                // überläuft, setze ihn wieder an den Anfang
                i = (i + 1) % this.size();
          }
        }
        output += ")";
        return output;
    }

    public static void main(String[] args) {

        /* Teste alle Ring Methoden */

        Ring<Boolean> rBoo = new Ring<>(20);
        Ring<Integer> rInt = new Ring<>(4);
        Ring<Double> rDou = new Ring<>(0);

        System.out.println("\n✔ Neuer Ring<Boolean> mit Capacity " + rBoo.capacity());
        System.out.println("✔ Neuer Ring<Integer> mit Capacity " + rInt.capacity());
        System.out.println("✔ Neuer Ring<Double> mit Capacity " + rDou.capacity() + "\n");

        System.out.println("✔ Teste Methoden für Ring<Integer>\n");
        rInt.add(8);    // Ring(8)
        System.out.println("add\t : \t" + rInt);
        rInt.add(3);    // Ring(8, 3)
        System.out.println("add\t : \t" +rInt);
        rInt.add(5);    // Ring(8, 3, 5)
        System.out.println("add\t : \t" +rInt);

        // Ersetze 8 durch 7 und mache 3 zum aktuellen Element
        rInt.set(7);    // Ring(3, 5, 7)
        System.out.println("set\t : \t" +rInt);

        // Füge 4 links neben dem aktuellen Element ein
        rInt.add(4);    // Ring(3, 5, 7, 4)
        System.out.println("add\t : \t" +rInt);
        System.out.println("size\t : \t" + rInt.size());

        // Mache den linken Nachbarn des aktuellen Elements zum neuen akt. El.
        rInt.back();    // Ring(4, 3, 5, 7)
        System.out.println("back\t : \t" +rInt);
        rInt.back();    // Ring(7, 4, 3, 5)
        System.out.println("back\t : \t" +rInt);

        // Lösche das akt. El. und mache den rechten Nachbarn zum neuen akt. El.
        rInt.remove();  // Ring(4, 3, 5)
        System.out.println("remove\t : \t" +rInt);
        rInt.remove();  // Ring(3, 5)
        System.out.println("remove\t : \t" +rInt);
        rInt.remove();  // Ring(5)
        System.out.println("remove\t : \t" +rInt);
        rInt.remove();  // Ring()
        System.out.println("remove\t : \t" +rInt);
        System.out.println("size\t : \t" + rInt.size());

        System.out.println("\n✔ Teste Methoden für Ring<Boolean>\n");
        rBoo.add(true);     // Ring(true) -> Sysout: Ring(true)
        System.out.println("add\t : \t" +rBoo);
        rBoo.add(false);    // Ring(true, false)
        System.out.println("add\t : \t" +rBoo);
        System.out.println("size\t : \t" + rBoo.size());
        rBoo.back();        // Ring(false, true)
        System.out.println("back\t : \t" +rBoo);
        rBoo.set(true);     // Ring(true, true)
        System.out.println("set\t : \t" +rBoo);
        rBoo.remove();      // Ring(true)
        System.out.println("remove\t : \t" +rBoo);
        rBoo.remove();      // Ring()
        System.out.println("remove\t : \t" +rBoo);
        System.out.println("size\t : \t" + rBoo.size());

        System.out.println("\n✔ Teste Fehler in Methode für Ring<Double>\n");
        // Ring mit einer Kapazität wirft eine Exception bei add()
        try {
          rDou.add(Math.PI);
        } catch(IllegalArgumentException ex) {
          System.out.println("add\t : \tfull ring throws IllegalArgumentException");
        }
        // Ring mit keinem Element wirft eine Exception bei remove()
        try {
          rDou.remove();
        } catch(NoSuchElementException ex) {
          System.out.println("remove\t : \tempty ring throws NoSuchElementException");
        }
        // Ring mit keinem Element wirft eine Exception bei back()
        try {
          rDou.back();
        } catch(NoSuchElementException ex) {
          System.out.println("back\t : \tempty ring throws NoSuchElementException");
        }
        // Ring mit keinem Element wirft eine Exception bei set()
        try {
          rDou.set(Math.PI);
        } catch(NoSuchElementException ex) {
          System.out.println("set\t : \tempty ring throws NoSuchElementException");
        }

        System.out.println("\n✔ Neuer Ring<Ring<String>> mit Capacity 5x5\n");

        System.out.println("✔ Ausgabe für Ring<Ring<String>>\n");

        // Erstelle einen Ring von Ringen mit Strings
        Ring<Ring<String>> outer = new Ring<>(5);
        for(int i = 0; i < 5; i++) {
            Ring<String> inner = new Ring<>(5);
            for(int j = 0; j < 5; j++) {
                inner.add(String.format("R%dE%d", i, j));
            }
            outer.add(inner);
        }
        System.out.println(outer);

        /* Teste alle statischen Rings Methoden */

        Ring<Integer> r = new Ring<>(8);

        System.out.println("\n✔ Teste statische Methoden für Ring<Integer>\n");

        r.add(1);  // Ring(1)
        r.add(2);  // Ring(1, 2)
        r.add(7);  // Ring(1, 2, 7)
        r.add(4);  // Ring(1, 2, 7, 4)
        r.add(7);  // Ring(1, 2, 7, 4, 7)
        r.add(8);  // Ring(1, 2, 7, 4, 7, 8)
        r.add(7);  // Ring(1, 2, 7, 4, 7, 8, 7)
        System.out.println("add X-mal\t : \t" +r);
        Rings.removeAll(r, 7);  // Ring(1, 2, 4, 8)
        System.out.println("removAll, 7\t : \t" +r);
        Rings.removeAll(r, 1);  // Ring(2, 4, 8)
        System.out.println("removAll, 1\t : \t" +r);
        Rings.removeAll(r, 4);  // Ring(2, 8)
        System.out.println("removAll, 4\t : \t" +r);
        Rings.removeAll(r, 2);  // Ring(8)
        System.out.println("removeAll, 2\t : \t" +r);

        // Konstruiere zwei Ringe, um merge zu testen
        Ring<Integer> r1 = new Ring<>(20);
        for (int i = 0; i < 10; i++) {
          r1.add(i + 3);
        }  // Ring(3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        r1.back();      // Ring(12, 3, 4, 5, 6, 7, 8, 9, 10, 11)
        r1.set(33);     // Ring(3, 4, 5, 6, 7, 8, 9, 10, 11, 33)
        r1.remove();    // Ring(4, 5, 6, 7, 8, 9, 10, 11, 33)
        r1.back();      // Ring(33, 4, 5, 6, 7, 8, 9, 10, 11)
        r1.back();      // Ring(11, 33, 4, 5, 6, 7, 8, 9, 10)
        Ring<Integer> r2 = new Ring<>(5);
        for (int i = 0; i < 5; i++) {
          r2.add(i * 12);
        }   // Ring(0, 12, 24, 36, 48)
        r2.set(42);     // Ring(12, 24, 36, 48, 42)
        r2.back();      // Ring(42, 12, 24, 36, 48)
        r2.back();      // Ring(48, 42, 12, 24, 36)

        System.out.println("\n✔ Teste merge für zwei Ringe Ring<Integer>\n");

        System.out.println(r1);
        System.out.println("+" + r2);
        Ring<Integer> merged = Rings.merge(r1, r2);
        // Ring(11, 33, 4, 5, 6, 7, 8, 9, 10, 48, 42, 12, 24, 36)
        System.out.println("=" + merged);
        System.out.println();
    }
}

class Rings {

    static void removeAll(Ring<?> ring, Object obj) {
        int size = ring.size();
        int i = 0;

        // Prüfe, ob der Ring nicht leer ist
        if (size == i) throw new NoSuchElementException("Illegale Operation: Ring ist leer");

        // Solange der Laufindex i nicht größer als size ist
        while(i < size) {
            // Entferne das akt. El., wenn es mit obj übereinstimmt
            if (ring.data[(ring.pivot + i) % ring.size()].equals(obj)) {
                if (size == 1) ring.remove();
                else {
                    // Setze den Zeiger durch Modulo Operationen auf das Element
                    // des Speicherarrays mit Index i und entferne dieses Element.
                    // Im Anschluss wird der Zeiger wieder zurück auf das zuvor
                    // betrachtete Element gesetzt und size verringert
                    ring.pivot = (ring.pivot + i) % ring.size();
                    ring.remove();
                    ring.pivot = (ring.pivot + ring.size - i) % ring.size;
                }
                size--;
            // Ansonsten erhöhe den Laufindex
            } else i++;
        }
    }

    static <T> Ring<T> merge(Ring<T> r1, Ring<T> r2) {
        // Erzeuge einen neuen Ring, der die Kapazität der Größen der beiden Ringe r1, r2 hat
        Ring<T> m = new Ring<>(r1.size() + r2.size());

        // Iteriere über beide Ringe und füge sequentiell jedes Element zum neuen Ring m hinzu
        for(int i = 0; i < r1.size(); i++) m.add(r1.data[(r1.pivot + i) % r1.size]);
        for(int i = 0; i < r2.size(); i++) m.add(r2.data[(r2.pivot + i) % r2.size]);
        return m;
    }
}