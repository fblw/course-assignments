# ==== Dijkstra =====

def minDistance(dist, q):
    min_node = q[0]  # Annahme, dass erstes Element das kleinste ist
    for node in q:  # Vergleich und ggf. Aktualisieren des min. Wertes
        if dist[node] != -1 and (dist[node] < dist[min_node] or dist[min_node] == -1):
            min_node = node
    return min_node


def dijkstra(g, s):
    graph = g
    nodes = range(len(graph))
    dist = {}  # Dictionary zum Speichern der Distanz
    prev = {}  # Speichern von zuvor durchlaufenen Knoten
    q = []
    start = s - 1  # Knotennummerierung fängt bei 1 an, Matrixindex bei 0

    # setze alle Knoten auf unendlich (isoliert)
    for node in nodes:
        dist[node] = -1
        prev[node] = None
        q.append(node)

    dist[start] = 0  # Startknoten hat Distanz 0

    while len(q):
        # finde Knoten mit min. Distanz und lösche ihn aus der Queue
        u = minDistance(dist, q)
        q.remove(u)

        # Nachbarn finden, falls Knoten verbunden sind
        neigbours = [i for i in nodes if graph[u][i] != -1]

        for n in neigbours:  # Aktualisieren der Werte der Nachbarn
            tmp = dist[u] + graph[u][n]
            if tmp < dist[n] or dist[n] == -1:
                dist[n] = tmp
                prev[n] = u

    # finde Pfade aller Knoten aus dem prev Dictionary
    paths = []
    for n in nodes:
        path = [n]
        prev_node = n
        while prev_node is not None:
            prev_node = prev.get(prev_node, None)
            if prev_node is not None:
                path = [prev_node] + path

        paths.append([p + 1 for p in path])  # Zurückkonvertieren in Knotennummerierung

    return paths


# ==== Auswertung =====

def pathCosts(l, z, g):
    path = None
    for p in l:  # für alle kürzesten Pfade in l
        if len(p) and p[-1] == z:  # finde den Pfad mit Zielknoten z
            path = p
    if not path: 
        return None  # falls kein Pfad gefunden wird, gib None aus

    cost = 0
    for i in range(len(path) - 1):  # Summieren von Kantengewichten
        cost = cost + g[path[i] - 1][path[i + 1] - 1]
    return cost  # Gesamtkosten des kürzesten Pfades von Start- zu Zielknoten


# ==== Test =====

print('Dijkstra test case: start at 4, go to 2')
graph = [
    [-1, 3, -1, -1, -1, -1, -1],
    [1, -1, -1, -1, -1, -1, -1],
    [-1, -1, -1, -1, -1, -1, -1],
    [4, 9, 5, -1, 2, -1, -1],
    [-1, -1, -1, 17, -1, 12, 8],
    [-1, -1, -1, -1, -1, -1, -1],
    [-1, -1, -1, -1, -1, -1, -1]
]
dresult = dijkstra(graph, 4)

costs = pathCosts(dresult, 2, graph)
print('Total cost:')
print(costs)  ## 7