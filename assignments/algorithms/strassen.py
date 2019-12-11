# ==== Strassen ====

from math import ceil, log

def showMatrix(m):
    if m==None: return
    for line in m:
        print('|', end='')
        i = 0
        for value in line:
            if (i > 0):
                print(' ', end='')
            print(value, end='')
            i = i + 1
        print("|")

def matMultDef(a, b):
    """ Berechnet das Produkt der Matrizen a, b iterativ nach Definition. """
    
    a_rows, a_cols, b_cols = len(a), len(a[0]), len(b[0]) # Variablennamen für Reihen/Spalten von a,b

    c = [[0 for i in range(b_cols)] for j in range(a_rows)] # 0-gefüllte Matrix c der Dimension b_cols*a_rows
    
    # Durchführung der Matrixmultiplikation per Definition für Reihen von a, Spalten von b, Spalten von a
    for i in range(a_rows):
        for j in range(b_cols):
            for k in range(a_cols):
                c[i][j] += (a[i][k] * b[k][j])

    result = c
    return result

def matMultSA(a, b):
    """ Multipliziert die beiden Matrizen a, b nach Strassen.
    Bevor die Berechnung erfolgen kann, werden die Matrizen angepasst. 
    Das Produkt wird durch den Aufruf der Funktion matMultSA_rec rekursiv berechnet.
    Der Rückgabewert ist das Resultat der Multiplikation. """
    
    # Dieser Block wird ausgeführt, falls eine Matrizenmultiplikation möglich ist
    try:
        a_rows, a_cols, b_rows, b_cols = len(a), len(a[0]), len(b), len(b[0]) # Variablennamen für Reihen/Spalten von a,b
        
        # Verursache ein ValueError für falsch angegebenen Matrizen
        if (a_cols != b_rows):
            raise ValueError("Matrizenmultiplikation für Matrizen mit diesen Dimensionen nicht möglich.\n>>> a.shape\n({0}, {1})\n>>> b.shape\n({2}, {3})\n".format(a_rows, a_cols, b_rows, b_cols))

        print("\n#### Matrizenmultiplikation nach Strassen ####\n")
        print('Parameter a:')
        showMatrix(a)
        print('Parameter b:')
        showMatrix(b)

        # Falls Matrizen nicht quadratisch sind, sollen sie auf die nächste Zweierpotenz der größten Dimension von a oder b 
        # mit Hilfe der Funktionen makeSqMatrix mit Nullen aufgefüllt werden
        if (a_rows != a_cols or b_rows != b_cols):
            n = 2**int(ceil(log(max(a_rows, a_cols, b_cols),2)))
            print("\n+ Implementierung nur für quadratsiche Matrizen definiert\n => Erweitere Matrizendimensionen auf nächste Zweierpotenz...\n => n={0}".format(n))
            print(" => Befülle Matrizen mit Nullen...\n")
            makeSqMatrix(a, n)
            makeSqMatrix(b, n)
            print('Parameter a:')
            showMatrix(a)
            print('Parameter b:')
            showMatrix(b)
        
        c = matMultSA_rec(a, b)

        print("\n+ Ergebnismatrix vor Anpassung von Zeilen- und Spaltenanzahl:\n")
        showMatrix(c)
        result = [row[:b_cols] for row in c[:a_rows]] # Redundante Zeilen und Spalten werden durch Slices vor der Ausgabe entfernt
        print("\n+ Ergebnis der Multiplikation:\n")
        showMatrix(result)
        print()
        
        return result

    # Dieser Block wird ausgeführt, falls ein ValueError im try Block verursacht wurde
    except ValueError:
        import sys, traceback
        etype, evalue, etb = sys.exc_info()
        traceback.print_stack(limit=-1)
        traceback.print_exception(etype=etype, value=evalue, tb=None)

def matMultSA_rec(a, b):
    """ Rekursive Hilfsfunktion für den Strassen-Algorithmus. """

    n = len(a)
    mitte = n//2 # Mitte der Matrix zur Teilung definieren

    if n <= 2: # Bei Basisfall Matrixmultiplikation nach Definition anwenden

        return matMultDef(a, b)
    else:
        # Zusammenstellen der Hilfsmatrizen p-v (nach Folie 29 im Skript 04-Teile-und-Hersche.pdf, AuD)
        p = matMultSA_rec(add(partSqMatrix(a,1, mitte), partSqMatrix(a,4, mitte)), add(partSqMatrix(b,1, mitte), partSqMatrix(b,4, mitte)))
        q = matMultSA_rec(add(partSqMatrix(a,3, mitte), partSqMatrix(a,4, mitte)), partSqMatrix(b,1, mitte))
        r = matMultSA_rec(partSqMatrix(a,1, mitte), sub(partSqMatrix(b,2, mitte), partSqMatrix(b,4, mitte)))
        s = matMultSA_rec(partSqMatrix(a,4, mitte), sub(partSqMatrix(b,3, mitte), partSqMatrix(b,1, mitte)))
        t = matMultSA_rec(add(partSqMatrix(a,1, mitte), partSqMatrix(a,2, mitte)), partSqMatrix(b,4, mitte))
        u = matMultSA_rec(sub(partSqMatrix(a,3, mitte), partSqMatrix(a,1, mitte)), add(partSqMatrix(b,1, mitte), partSqMatrix(b,2, mitte)))
        v = matMultSA_rec(sub(partSqMatrix(a,2, mitte), partSqMatrix(a,4, mitte)), add(partSqMatrix(b,3, mitte), partSqMatrix(b,4, mitte)))

        c11 = sub(add(add(p, s), v), t)
        c12 = add(r, t)
        c21 = add(q, s)
        c22 = sub(add(add(p, r), u), q)

        print("\n+ Berechne rekursiv C...\n")
        showMatrix([['c11', 'c12'], ['c21', 'c22']])
        print("\n...mit c11 = p + s - t + v")
        print('Parameter p:')
        showMatrix(p)
        print('Parameter s:')
        showMatrix(s)
        print('Parameter t:')
        showMatrix(t)
        print('Parameter v:')
        showMatrix(v)
        print("...mit c12 = r + t")
        print('Parameter r:')
        showMatrix(r)
        print('Parameter t:')
        showMatrix(t)
        print("...mit c21 = q + s")
        print('Parameter q:')
        showMatrix(q)
        print('Parameter s:')
        showMatrix(s)
        print("...mit c22 = p + r - q + u")
        print('Parameter p:')
        showMatrix(p)
        print('Parameter r:')
        showMatrix(r)
        print('Parameter q:')
        showMatrix(t)
        print('Parameter u:')
        showMatrix(v)
        print('\n => c11, c12, c21, c22:\n')
        showMatrix(c11)
        print()
        showMatrix(c12)
        print()
        showMatrix(c21)
        print()
        showMatrix(c22)

        # Zusammensetzen der Ergebnismatrix c aus den vier Submatrizen c[i,j]
        c = [[0 for j in range(n)] for i in range(n)]
        for i in range(mitte):
            for j in range(mitte):
                c[i][j] = c11[i][j]
                c[i][j + mitte] = c12[i][j]
                c[i + mitte][j] = c21[i][j]
                c[i + mitte][j + mitte] = c22[i][j]
        return c

def add(a, b):
    """ Addiert die beiden Matrizen a, b komponentenweise.
    Gibt das Resultat der Addition als neue Matrix c aus. """

    n = len(a)
    c = [[0 for j in range(n)] for i in range(n)]
    for i in range(n):
        for j in range(n):
            c[i][j] = a[i][j] + b[i][j]
    return c

def sub(a, b):
    """ Subtrahiert die beiden Matrizen a, b komponentenweise.
    Gibt das Resultat der Subtraktion als neue Matrix c aus. """

    n = len(a)
    c = [[0 for j in range(n)] for i in range(n)]
    for i in range(n):
        for j in range(n):
            c[i][j] = a[i][j] - b[i][j]
    return c

def makeSqMatrix(a, n):
    """ Füllt die Matrix a mit Nullen auf, bis diese 
    die quadratische Form (n, n) hat. """

    for i in range(len(a)):
        a[i] += [0]*(n-len(a[i]))
        for j in range(n-len(a)):
            a.append([0]*n)

def partSqMatrix(a, x, cut):
    """ Funktion zum Teilen der quadratischen Matrizen mit Hilfe von Slices. 
    Der Parameter x kann einen der Werte 1,2,3,4 annehmen. Er legt fest, welcher der vier
    Blockmatrizen c11, c12, c21, c22 (resp. 1,2,3,4) zurückgegeben wird.
    Der Parameter cut ist die Stelle, an der die Matrix a geteilt wird. """

    if x == 1:
        return [row[:cut] for row in a[:cut]]
    elif x == 2:
        return [row[cut:] for row in a[:cut]]
    elif x == 3:
        return [row[:cut] for row in a[cut:]]
    elif x == 4:
        return [row[cut:] for row in a[cut:]]

""" Testfälle """
a, b = [[3, 2, 1],[1, 0, 2]], [[1, 2],[0, 1],[4, 0]]
# a2, b2, = [[2,3,4], [2,4,5]], [[0,0], [1,2]]
# a3, b3 = [[2,1], [3,2], [4,1]], [[0,0], [1,2]]

matMultSA(a,b)
# matMultSA(a2, b2)
# matMultSA(a3, b3)
