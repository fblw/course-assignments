# ==== Kruskal =====

def find(x, forrest):
    # Finden der Wurzel von x durch "Path splitting"
    while forrest[x] != x:
        # verweise jedes Element auf dem Weg zum Wurzelelement auf dessen Großelternelement
        x, forrest[x] = forrest[x], forrest[forrest[x]]
    return x  # gib die Wurzel von x aus (nach Zuweisung ist hier x == forrest[x], also Wurzel des formalen Parameters x)


def union(u_root, v_root, forrest, rank):
    if rank[v_root] < rank[u_root]:  # falls der Baum mit Wurzel v_root kleiner ist
        forrest[v_root] = u_root  # füge Baum mit v_root an Baum mit u_root
    else:
        # falls die Bäume von v_root und u_root die gleiche Tiefe haben, erhöhe den Rang von v_root
        if rank[v_root] == rank[u_root]:
            rank[v_root] += 1
        forrest[u_root] = v_root  # sonst füge Baum mit u_root an Baum mit v_root


def kruskal(g):
    n = len(g)  # Anzahl der Knoten von g

    # macht eine Kantenliste aus g und sortiert diese nach Gewicht
    edges = list()
    for i in range(1, n):
        for j in range(i):
            if g[i][j] != -1:
                edges.append((i, j, g[i][j]))  # konstruiert eine Liste der Kanten e bestehend aus Tupeln (u, v, w(e))

    edges = sorted(edges, key=lambda k: k[2])  # sortiert die Kanten nach Gewicht

    forrest = [i for i in range(n)]  # pseudo make-set

    rank = [0] * n  # Liste, um den Rang des zugehörigen Baumes an der Position des Wurzelelements zu speichern

    spanning_tree = [[-1] * n for i in range(n)]  # Adjazenzmatrix des resultierenden minimalen Spannbaums

    for e in edges:
        u = e[0]  # erster Knoten der Kante e
        v = e[1]  # zweiter Knoten der Kante e
        w = e[2]  # Gewicht der Kante e

        u_root = find(u, forrest)
        v_root = find(v, forrest)

        if (u_root != v_root):  # vermeide Kreise
            union(u_root, v_root, forrest,
                  rank)  # verbindet die Wurzelelemente von u und v unter Berücksichtigung des Ranges
            spanning_tree[u][v] = w
            spanning_tree[v][u] = w  # doppelte Zuweisung, da der minimale Spannbaum ein ungerichteter Graph ist

    return spanning_tree


# ==== Auswertung =====

def spanTreeWeight(a):
    weight = 0
    for i in range(1, len(a)):
        for j in range(i):
            if a[i][j] != -1:
                weight += a[i][j]  # summiert jedes Element auf, das ungleich -1 ist und unterhalb der Diagonalen liegt
    return weight  # gibt die Summe der Kantengewichte als Resultat aus


# ==== Test =====

print('Kruskal Test')
kresult = kruskal([
    [-1, 8, 2, 1],
    [8, -1, 6, 5],
    [2, 6, -1, 3],
    [1, 5, 3, -1]
])
weight = spanTreeWeight(kresult)
print('MST overall weight:')
print(weight)  # 8