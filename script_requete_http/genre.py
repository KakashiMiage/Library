import requests
import json

BASE_URL = "http://localhost:8080/api/genres"

def print_json(response):
    try:
        parsed = response.json()
        print(json.dumps(parsed, indent=4, ensure_ascii=False))
    except Exception as e:
        print(f"Erreur de parsing JSON: {e}")
        print(response.text)

def create_genre(name):
    print("Création d'un Genre")
    payload = {
        "genreName": name
    }
    response = requests.post(BASE_URL, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("genreId") if response.status_code == 201 else None

def get_all_genres():
    print("Récupération de tous les Genres")
    response = requests.get(BASE_URL)
    print(f"Status Code: {response.status_code}")
    print_json(response)

def get_genre_by_id(genre_id):
    print(f"Récupération du Genre ID {genre_id}")
    response = requests.get(f"{BASE_URL}/{genre_id}")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def update_genre(genre_id, name):
    print(f"Mise à jour du Genre ID {genre_id}")
    payload = {
        "genreName": name
    }
    response = requests.put(f"{BASE_URL}/{genre_id}", json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)

def delete_genre(genre_id):
    print(f"Suppression du Genre ID {genre_id}")
    response = requests.delete(f"{BASE_URL}/{genre_id}")
    print(f"Status Code: {response.status_code}")

def count_genres():
    print("Comptage de tous les Genres")
    response = requests.get(f"{BASE_URL}/count")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def search_genre_by_name(name):
    print(f"Recherche d'un Genre par nom '{name}'")
    params = {
        "name": name
    }
    response = requests.get(f"{BASE_URL}/search", params=params)
    print(f"Status Code: {response.status_code}")
    print_json(response)

def get_books_by_genre(genre_id):
    print(f"Récupération des Livres du Genre ID {genre_id}")
    response = requests.get(f"{BASE_URL}/{genre_id}/books")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def run_tests():
    print("==============================")
    print("DÉMARRAGE DES TESTS COMPLETS API GENRE")
    print("==============================")

    # Étape 1 : Créer un Genre
    genre_id = create_genre("Science-Fiction")
    if not genre_id:
        print("Erreur lors de la création du Genre. Arrêt des tests.")
        return

    # Étape 2 : Récupérer tous les Genres
    get_all_genres()

    # Étape 3 : Récupérer le Genre par ID
    get_genre_by_id(genre_id)

    # Étape 4 : Mettre à jour le Genre
    update_genre(genre_id, "Science-Fiction - Mise à Jour")

    # Étape 5 : Recherche par nom
    search_genre_by_name("Science-Fiction - Mise à Jour")

    # Étape 6 : Comptage des Genres
    count_genres()

    # Étape 7 : Vérification des livres liés au Genre (base vide, résultat attendu vide)
    get_books_by_genre(genre_id)

    # Étape 8 : Suppression du Genre
    delete_genre(genre_id)

    # Étape 9 : Vérifier la suppression du Genre
    get_genre_by_id(genre_id)

    print("==============================")
    print("FIN DES TESTS API GENRE")
    print("==============================")

if __name__ == "__main__":
    run_tests()
