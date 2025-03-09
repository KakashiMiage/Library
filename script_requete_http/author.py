import requests
import json

BASE_URL = "http://localhost:8080/authors"  # Remplace par l'URL réelle de ton API

def create_author(author_data):
    """Créer un nouvel auteur."""
    response = requests.post(f"{BASE_URL}", json=author_data)
    return response.json()

def find_all_authors():
    """Récupérer tous les auteurs."""
    response = requests.get(f"{BASE_URL}")
    return response.json()

def count_authors():
    """Compter le nombre total d'auteurs."""
    response = requests.get(f"{BASE_URL}/count")
    return response.json()

def get_author_by_id(author_id):
    """Récupérer un auteur par son ID."""
    response = requests.get(f"{BASE_URL}/{author_id}")
    return response.json()

def update_author(author_id, updated_data):
    """Mettre à jour un auteur."""
    response = requests.put(f"{BASE_URL}/{author_id}", json=updated_data)
    return response.json()

def delete_author(author_id):
    """Supprimer un auteur."""
    response = requests.delete(f"{BASE_URL}/{author_id}")
    return response.status_code

def get_author_by_name(first_name, last_name):
    """Rechercher un auteur par prénom et nom."""
    response = requests.get(f"{BASE_URL}/search", params={"firstName": first_name, "lastName": last_name})
    return response.json()

# Test des fonctions
if __name__ == "__main__":
    # Création d'un auteur de test
    test_author = {
        "authorFirstName": "Albert",
        "authorLastName": "Camus"
    }
    created_author = create_author(test_author)
    print("Auteur créé:", json.dumps(created_author, indent=2))

    # # Récupérer tous les auteurs
    # authors = find_all_authors()
    # print("Liste des auteurs:", json.dumps(authors, indent=2))

    # # Récupérer un auteur par ID
    # authors = find_all_authors()
    # if authors:
    #     author_id = authors[0]["authorId"]
    #     author = get_author_by_id(author_id)
    #     print("Auteur trouvé par ID:", json.dumps(author, indent=2))

    # # Mettre à jour un auteur
    # updated_data = {
    #     "authorFirstName": "Albert",
    #     "authorLastName": "Camus (Mise à jour)"
    # }
    # authors = find_all_authors()
    # if authors:
    #      author_id = authors[0]["authorId"]
    # updated_author = update_author(author_id, updated_data)
    # print("Auteur mis à jour:", json.dumps(updated_author, indent=2))

    # # Recherche par prénom et nom
    # search_results = get_author_by_name("Albert", "Camus")
    # print("Auteur trouvé par nom:", json.dumps(search_results, indent=2))

    # # Nombre total d'auteurs
    # total_authors = count_authors()
    # print("Nombre total d'auteurs:", json.dumps(total_authors, indent=2))

    # # Suppression de l'auteur
    # authors = find_all_authors()
    # if authors:
    #      author_id = authors[0]["authorId"]
    # delete_status = delete_author(author_id)
    # print(f"Auteur supprimé, statut HTTP: {delete_status}")
