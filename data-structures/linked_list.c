#include <stdio.h>
#include <stdlib.h>

typedef struct le {
    int value;
    struct le * next;
} listenelement;

typedef listenelement * list;

void insert(int v, list *li){
    listenelement * new;
    new = malloc(sizeof(listenelement));
    new->value = v;
    new->next = *li;
    *li = new;
};

int delete_head(list * li){
    if (*li == NULL) return -1;
    list old = *li;
    *li = old->next;
    free(old);
    return 0;
};

void delete_all(list *li) {
    list next;
    while (*li != NULL) {
        next = (*li)->next;
        free(*li);
        *li = next;
    }
};

int length(list li) {
    int count = 0;
    while (li != NULL) {
        count++;
        li = li->next;
    }
    return count;
};

void print_list(list li) {
    if(li == NULL) printf("leer");
    else
        while(li != NULL) {
            printf("%d", li->value);
            li = li->next;
        }
    printf("\n");
};

/* Löschen eines Elements aus der Liste anhand der Position */

int delete_pos(list *li, int pos) {
    if (pos == 0) {
        delete_head(li);
        return 0;
    }
    int count = 1;
    listenelement * tmp = *li;
    while (tmp != NULL) { 
        // Falls die Zählvariable count der Position pos entspricht, gib den Speicher frei 
        // und ordne die Liste, durch Umsetzen der Pointer um
        if (count == pos) {
            listenelement * removed = tmp->next;
            tmp->next = (tmp->next)->next;
            free(removed);
            return 0;
        }
        count++;
        tmp = tmp->next;
    }
    // Falls die Position pos keinem Index count der Liste entspricht, gib -1 aus
    return -1;
}

/* Löschen eines Elements aus der Liste */

int delete_elem(list * li, int e) {
    if (*li == NULL) return -1;
    
    listenelement * tmp = *li;
    int pos;
    for(pos = 0; tmp != NULL; tmp = tmp->next, pos++) {
        // Falls der Wert in der Liste dem Wert e entspricht, rufe delete_pos mit der Zählvariable pos auf
        if (tmp->value == e) {
            delete_pos(li, pos);
            return 0;
        }
    }
    // Falls der Wert nicht in der Liste enthalten ist, gib -1 aus
    return -1;
}

/* insertionSort */

// Deklarationen der Hilfsfunktionen

int get_val(list * li, int pos);
void change_val(list * li, int pos, int val);

// Definition des Sortieralgorithmus

void sort(list * li, int m) {
    if (m < 0) {
        // Falls m<0: Ersetze jeden Wert mit dem dazugehörigen Absolutwert und rufe sort mit m=0 auf
        listenelement * tmp = *li;
        for(; tmp != NULL; tmp = tmp->next) if (tmp->value < 0) tmp->value = abs(tmp->value);
        sort(li, 0);
    } else {
        // Falls m>=0: Wende InsertionSort als in-place Algorithmus an (S.18, 05-Suchen-und-Sortieren.pdf, AuD19)
        int i;
        for(i = 0; i < length(*li); i++) {
            int val = get_val(li, i);
            int pos = i;
            while ( (pos > 0) && (get_val(li, pos-1) > val) ) {
                change_val(li, pos, get_val(li, pos-1));
                pos--;
            }
            if (pos != i) change_val(li, pos, val);
        }
    }
}

// Definitionen der Hilfsfunktionen

int get_val(list * li, int pos){
    /* Gibt den Wert an der Position pos der Liste li aus */

    if (*li == NULL) return -1;
    
    listenelement * tmp = *li;
    int i;
    for(i = 0; tmp != NULL; tmp = tmp->next, i++) {
        if (i == pos) {
            return tmp->value;
        }
    }
    return -1;
}

void change_val(list * li, int pos, int val){
    /* Ersetzt den Wert an der Stelle pos in der Liste li mit dem Wert val */

    listenelement * tmp = *li;
    int i;
    for(i = 0; tmp != NULL; tmp = tmp->next, i++) {
        if (i == pos) {
            tmp->value = val;
        }
    }
}

/* Tests innerhalb der main-Funktion */

int main(int argc, char *argv[]) {

    printf("\n#### TEST A ####\n");

    list li;
    printf("Deklarierte Liste: ");
    print_list(li); // Da die Liste uninitialisiert ist, wird hier beim Compilieren eine Warnung ausgeben
    printf("Länge: %d\n", length(li));
    printf("\nFüge Elemente zur Liste hinzu...");
    insert(-1, &li);
    insert(2, &li);
    insert(3, &li);
    insert(-4, &li);
    insert(5, &li);
    printf("\nInitialisierte Liste: ");
    print_list(li);
    printf("Länge: %d\n", length(li));
    printf("\nEntferne das erste Elemente...");
    delete_head(&li);
    printf("\nListe: ");
    print_list(li);
    printf("\nEntferne das Elemente an Position 2...");
    delete_pos(&li, 2);
    printf("\nListe: ");
    print_list(li);
    printf("\nEntferne das Elemente an Position 10...");
    printf("\nRückgabewert: %i", delete_pos(&li, 10));
    printf("\nListe: ");
    print_list(li);
    printf("\nGib das Elemente an Position 0 zurück...");
    printf("\nErstes Element: %i", get_val(&li, 0));
    printf("\n\nEntferne das Element %i...", get_val(&li, 1));
    delete_elem(&li, get_val(&li, 1));
    printf("\nListe: ");
    print_list(li);
    printf("\nEntferne das Elemente -97...");
    printf("\nRückgabewert: %i", delete_elem(&li, -97));
    printf("\nListe: ");
    print_list(li);
    printf("\nEntferne alle Elemente...");
    delete_all(&li);
    printf("\nListe: ");
    print_list(li);
    printf("\n");

    printf("\n#### TEST B ####\n");

    printf("Füge neue Elemente zur Liste hinzu...");
    insert(1, &li);
    insert(-2, &li);
    insert(-3, &li);
    insert(4, &li);
    insert(-5, &li);
    printf("\nListe: ");
    print_list(li);
    printf("\nSortiere Elemente mit m=0...\n");
    sort(&li, 0);
    print_list(li);
    printf("\nSortiere Elemente mit m=-1...\n");
    sort(&li, -1);
    print_list(li);
    printf("\n");

    return 0;
};