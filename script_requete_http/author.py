import requests
import json

# URLs de base
BASE_AUTHOR_URL = "http://localhost:8080/api/authors"
BASE_BOOK_URL = "http://localhost:8080/api/books"
BASE_TYPE_URL = "http://localhost:8080/api/types"
BASE_GENRE_URL = "http://localhost:8080/api/genres"
BASE_EDITOR_URL = "http://localhost:8080/api/editors"

def print_json(response):
    try:
        parsed = response.json()
        print(json.dumps(parsed, indent=4, ensure_ascii=False))
    except Exception as e:
        print(f"Erreur de parsing JSON: {e}")
        print(response.text)

# ====================
# CRUD sur Author
# ====================
def create_author(first_name, last_name):
    print("Création d'un auteur")
    payload = {
        "authorFirstName": first_name,
        "authorLastName": last_name
    }
    response = requests.post(BASE_AUTHOR_URL, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("authorId") if response.status_code == 201 else None

def get_author_by_id(author_id):
    print(f"Récupération de l'auteur par ID {author_id}")
    response = requests.get(f"{BASE_AUTHOR_URL}/{author_id}")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def update_author(author_id, first_name, last_name):
    print(f"Mise à jour de l'auteur ID {author_id}")
    payload = {
        "authorFirstName": first_name,
        "authorLastName": last_name
    }
    response = requests.put(f"{BASE_AUTHOR_URL}/{author_id}", json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)

def delete_author(author_id):
    print(f"Suppression de l'auteur ID {author_id}")
    response = requests.delete(f"{BASE_AUTHOR_URL}/{author_id}")
    print(f"Status Code: {response.status_code}")

def get_all_authors():
    print("Récupération de tous les auteurs")
    response = requests.get(BASE_AUTHOR_URL)
    print(f"Status Code: {response.status_code}")
    print_json(response)

def count_authors():
    print("Nombre total d'auteurs")
    response = requests.get(f"{BASE_AUTHOR_URL}/count")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def search_author_by_fullname(first_name, last_name):
    print(f"Recherche de l'auteur par prénom '{first_name}' et nom '{last_name}'")
    params = {
        "firstName": first_name,
        "lastName": last_name
    }
    response = requests.get(f"{BASE_AUTHOR_URL}/search/fullname", params=params)
    print(f"Status Code: {response.status_code}")
    print_json(response)

def search_authors_by_lastname(last_name):
    print(f"Recherche des auteurs par nom '{last_name}'")
    response = requests.get(f"{BASE_AUTHOR_URL}/search/lastname/{last_name}")
    print(f"Status Code: {response.status_code}")
    print_json(response)

def get_books_by_author(author_id):
    print(f"Récupération des livres de l'auteur ID {author_id}")
    response = requests.get(f"{BASE_AUTHOR_URL}/{author_id}/books")
    print(f"Status Code: {response.status_code}")
    print_json(response)

# ====================
# CRUD sur dépendances (Type / Genre / Editor / Book)
# ====================
def create_type(name):
    print("Création d'un type")
    payload = {"typeName": name}
    response = requests.post(BASE_TYPE_URL, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("typeId") if response.status_code == 201 else None

def create_genre(name):
    print("Création d'un genre")
    payload = {"genreName": name}
    response = requests.post(BASE_GENRE_URL, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("genreId") if response.status_code == 201 else None

def create_editor(name, siret, type_ids):
    print("Création d'un éditeur")
    payload = {
        "editorName": name,
        "editorSIRET": siret,
        "types": [{"typeId": type_id} for type_id in type_ids]
    }
    response = requests.post(BASE_EDITOR_URL, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("editorId") if response.status_code == 201 else None

def create_book(title, publication_date, description, pages, author_id, editor_id, type_id, genre_ids):
    print("Création d'un book")
    payload = {
        "bookTitle": title,
        "bookPublicationDate": publication_date,
        "bookDescription": description,
        "numberOfPages": pages,
        "author": {"authorId": author_id},
        "editor": {"editorId": editor_id},
        "type": {"typeId": type_id},
        "genres": [{"genreId": gid} for gid in genre_ids]
    }
    response = requests.post(BASE_BOOK_URL, json=payload)
    print(f"Status Code: {response.status_code}")
    print_json(response)
    return response.json().get("isbn") if response.status_code == 201 else None

def create_books_for_author(author_id):
    print(f"Création de livres pour l'auteur ID {author_id}")

    # Création des dépendances nécessaires
    type_id = create_type("Roman")
    genre_id = create_genre("Aventure")
    editor_id = create_editor("Hachette", 98765432101234, [type_id])

    # Création d'un livre
    book_id = create_book(
        title="Voyage au centre de la Terre",
        publication_date="1864-01-01",
        description="Un voyage extraordinaire vers les profondeurs de la Terre.",
        pages=300,
        author_id=author_id,
        editor_id=editor_id,
        type_id=type_id,
        genre_ids=[genre_id]
    )

    if book_id:
        print(f"Livre créé avec succès pour l'auteur ID {author_id} !")
    else:
        print(f"Échec de la création du livre pour l'auteur ID {author_id}.")

# ====================
# Lancement des tests
# ====================
def run_tests():
    print("==============================")
    print("DÉMARRAGE DES TESTS COMPLETS API AUTHOR")
    print("==============================")

    # Créer un auteur
    author_id = create_author("Jules", "Verne")
    if not author_id:
        print("Échec de la création de l'auteur. Arrêt des tests.")
        return

    # Créer un livre pour cet auteur
    create_books_for_author(author_id)

    # Récupérer tous les auteurs
    get_all_authors()

    # Récupérer l'auteur par ID
    get_author_by_id(author_id)

    # Mise à jour de l'auteur
    update_author(author_id, "Jules", "Verne - Révisé")

    # Recherche par nom complet
    search_author_by_fullname("Jules", "Verne - Révisé")

    # Recherche par nom de famille
    search_authors_by_lastname("Verne - Révisé")

    # Compter le nombre d'auteurs
    count_authors()

    # Récupérer les livres de l'auteur
    get_books_by_author(author_id)

    # Suppression de l'auteur
    delete_author(author_id)

    # Vérifier la suppression
    get_author_by_id(author_id)

    print("==============================")
    print("FIN DES TESTS API AUTHOR")
    print("==============================")

if __name__ == "__main__":
    run_tests()
