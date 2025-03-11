import requests
import json

API_BASE = "http://localhost:8080/api"
TYPE_URL = f"{API_BASE}/types"

def print_response(response):
    print(f"Status Code: {response.status_code}")
    try:
        print(json.dumps(response.json(), indent=2, ensure_ascii=False))
    except Exception:
        print(response.text)
    print("-" * 50)

# 1. Créer un Type
def create_type(type_data):
    print("Création d'un Type")
    response = requests.post(TYPE_URL, json=type_data)
    print_response(response)
    return response.json()

# 2. Récupérer tous les Types
def get_all_types():
    print("Liste de tous les Types")
    response = requests.get(TYPE_URL)
    print_response(response)
    return response.json()

# 3. Récupérer un Type par ID
def get_type_by_id(type_id):
    print(f"Détails du Type ID: {type_id}")
    response = requests.get(f"{TYPE_URL}/{type_id}")
    print_response(response)
    return response.json()

# 4. Rechercher un Type par nom
def search_type_by_name(type_name):
    print(f"Recherche du Type par nom: {type_name}")
    response = requests.get(f"{TYPE_URL}/search/name", params={"typeName": type_name})
    print_response(response)
    return response.json()

# 5. Compter les Types
def count_types():
    print("Compter le nombre de Types")
    response = requests.get(f"{TYPE_URL}/count")
    print_response(response)
    return response.json()

# 6. Mise à jour d'un Type
def update_type(type_id, updated_data):
    print(f"Mise à jour du Type ID: {type_id}")
    response = requests.put(f"{TYPE_URL}/{type_id}", json=updated_data)
    print_response(response)
    return response.json()

# 7. Récupérer les livres par Type ID
def get_books_by_type(type_id):
    print(f"Récupérer les livres liés au Type ID: {type_id}")
    response = requests.get(f"{TYPE_URL}/{type_id}/books")
    print_response(response)
    return response.json()

# 8. Récupérer les Types liés à un Genre (optionnel)
def get_types_by_genre(genre_id):
    print(f"Récupérer les Types liés au Genre ID: {genre_id}")
    response = requests.get(f"{TYPE_URL}/search/genre/{genre_id}")
    print_response(response)
    return response.json()

# 9. Supprimer un Type
def delete_type(type_id):
    print(f"Suppression du Type ID: {type_id}")
    response = requests.delete(f"{TYPE_URL}/{type_id}")
    print_response(response)
    return response.status_code

# MAIN TEST SEQUENCE
if __name__ == "__main__":
    print("DÉMARRAGE DES TESTS COMPLETS SUR L'API TYPES")

    # Étape 1 : Créer deux types
    type_1 = create_type({
        "typeName": "Essai"
    })
    type_2 = create_type({
        "typeName": "Biographie"
    })

    type1_id = type_1.get("typeId")
    type2_id = type_2.get("typeId")

    if not type1_id or not type2_id:
        print("Erreur : Impossible de créer les Types, arrêt du test.")
        exit(1)

    # Étape 2 : Vérifier tous les Types
    get_all_types()

    # Étape 3 : Récupérer un Type par ID
    get_type_by_id(type1_id)

    # Étape 4 : Rechercher un Type par nom
    search_type_by_name("Essai")

    # Étape 5 : Mise à jour du premier Type
    updated_type_payload = {
        "typeName": "Essai Révisé"
    }
    update_type(type1_id, updated_type_payload)

    # Étape 6 : Vérification après mise à jour
    get_type_by_id(type1_id)

    # Étape 7 : Compter le nombre de Types
    count_types()

    # Étape 8 : Récupérer les livres liés (aucun en théorie)
    get_books_by_type(type1_id)

    # Étape 9 : Rechercher les Types par Genre (optionnel si tu as une relation en base)
    # get_types_by_genre(1)  # Si tu as un Genre en base

    # Étape 10 : Suppression des Types
    delete_type(type1_id)
    delete_type(type2_id)

    # Vérification finale : liste et count
    get_all_types()
    count_types()

    print("TESTS COMPLETS TERMINÉS")
