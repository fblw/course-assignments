#include <stdio.h>
#include <stdlib.h>

typedef struct le {
    int value;
    struct le * next;
} listenelement;

typedef listenelement * list;

void insert(int v, list *l){
    listenelement * new;
    new = malloc(sizeof(listenelement));
    new->value = v;
    new->next = *l;
    *l = new;
};

int delete_head(list * l){
    if (*l == NULL) return -1;
    list old = *l;
    *l = old->next;
    free(old);
    return 0;
};

void delete_all(list *l) {
    list next;
    while (*l != NULL) {
        next = (*l)->next;
        free(*l);
        *l = next;
    }
};

int length(list l) {
    int count = 0;
    while (l != NULL) {
        count++;
        l = l->next;
    }
    return count;
};

void print_list(list l) {
    if(l == NULL) printf("leer");
    else
        while(l != NULL) {
            printf("%d", l->value);
            l = l->next;
        }
    printf("\n");
};

/* Löschen eines Elements aus der Liste anhand der Position */

int delete_pos(list *l, int pos) {
    if (pos == 0) {
        delete_head(l);
        return 0;
    }
    int count = 1;
    listenelement * tmp = *l;
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

int delete_elem(list * l, int e) {
    if (*l == NULL) return -1;
    
    listenelement * tmp = *l;
    int pos;
    for(pos = 0; tmp != NULL; tmp = tmp->next, pos++) {
        // Falls der Wert in der Liste dem Wert e entspricht, rufe delete_pos mit der Zählvariable pos auf
        if (tmp->value == e) {
            delete_pos(l, pos);
            return 0;
        }
    }
    // Falls der Wert nicht in der Liste enthalten ist, gib -1 aus
    return -1;
}

/* insertionSort */

// Deklarationen der Hilfsfunktionen

int get_val(list * l, int pos);
void change_val(list * l, int pos, int val);

// Definition des Sortieralgorithmus

void sort(list * l, int m) {
    if (m < 0) {
        // Falls m<0: Ersetze jeden Wert mit dem dazugehörigen Absolutwert und rufe sort mit m=0 auf
        listenelement * tmp = *l;
        for(; tmp != NULL; tmp = tmp->next) if (tmp->value < 0) tmp->value = abs(tmp->value);
        sort(l, 0);
    } else {
        // Falls m>=0: Wende InsertionSort als in-place Algorithmus an (S.18, 05-Suchen-und-Sortieren.pdf, AuD19)
        int i;
        for(i = 0; i < length(*l); i++) {
            int val = get_val(l, i);
            int pos = i;
            while ( (pos > 0) && (get_val(l, pos-1) > val) ) {
                change_val(l, pos, get_val(l, pos-1));
                pos--;
            }
            if (pos != i) change_val(l, pos, val);
        }
    }
}

// Definitionen der Hilfsfunktionen

int get_val(list * l, int pos){
    /* Gibt den Wert an der Position pos der Liste l aus */

    if (*l == NULL) return -1;
    
    listenelement * tmp = *l;
    int i;
    for(i = 0; tmp != NULL; tmp = tmp->next, i++) {
        if (i == pos) {
            return tmp->value;
        }
    }
    return -1;
}

void change_val(list * l, int pos, int val){
    /* Ersetzt den Wert an der Stelle pos in der Liste l mit dem Wert val */

    listenelement * tmp = *l;
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

    list l;
    printf("Deklarierte Liste: ");
    print_list(l); // Da die Liste uninitialisiert ist, wird hier beim Compilieren eine Warnung ausgeben
    printf("Länge: %d\n", length(l));
    printf("\nFüge Elemente zur Liste hinzu...");
    insert(-1, &l);
    insert(2, &l);
    insert(3, &l);
    insert(-4, &l);
    insert(5, &l);
    printf("\nInitialisierte Liste: ");
    print_list(l);
    printf("Länge: %d\n", length(l));
    printf("\nEntferne das erste Elemente...");
    delete_head(&l);
    printf("\nListe: ");
    print_list(l);
    printf("\nEntferne das Elemente an Position 2...");
    delete_pos(&l, 2);
    printf("\nListe: ");
    print_list(l);
    printf("\nEntferne das Elemente an Position 10...");
    printf("\nRückgabewert: %i", delete_pos(&l, 10));
    printf("\nListe: ");
    print_list(l);
    printf("\nGib das Elemente an Position 0 zurück...");
    printf("\nErstes Element: %i", get_val(&l, 0));
    printf("\n\nEntferne das Element %i...", get_val(&l, 1));
    delete_elem(&l, get_val(&l, 1));
    printf("\nListe: ");
    print_list(l);
    printf("\nEntferne das Elemente -97...");
    printf("\nRückgabewert: %i", delete_elem(&l, -97));
    printf("\nListe: ");
    print_list(l);
    printf("\nEntferne alle Elemente...");
    delete_all(&l);
    printf("\nListe: ");
    print_list(l);
    printf("\n");

    printf("\n#### TEST B ####\n");

    printf("Füge neue Elemente zur Liste hinzu...");
    insert(1, &l);
    insert(-2, &l);
    insert(-3, &l);
    insert(4, &l);
    insert(-5, &l);
    printf("\nListe: ");
    print_list(l);
    printf("\nSortiere Elemente mit m=0...\n");
    sort(&l, 0);
    print_list(l);
    printf("\nSortiere Elemente mit m=-1...\n");
    sort(&l, -1);
    print_list(l);
    printf("\n");

    return 0;
};