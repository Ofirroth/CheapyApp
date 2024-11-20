import numpy as np
import pandas as pd
import os
import sys
from sklearn.neighbors import NearestNeighbors
import json

# Load the user-item matrix from the CSV file
base_path = os.path.dirname(os.path.abspath(__file__))
file_path = os.path.join(base_path, "Server", "user_item_matrix.csv")
df = pd.read_csv('./user_item_matrix.csv')

# Remove the userId column to create the user-item matrix
user_item_matrix = df.drop('userId', axis=1).fillna(0)  # Replace NaNs with 0 (no interaction)

n_neighbors = min(6, len(user_item_matrix))  # Adjust n_neighbors to the number of samples in the matrix
knn = NearestNeighbors(n_neighbors=n_neighbors, metric='cosine')  # Use cosine similarity
knn.fit(user_item_matrix)

# Function to recommend top N items for a given user
def recommend_items(user_index, user_item_matrix, knn, top_n=6):
    # Find the nearest neighbors of the given user
    distances, indices = knn.kneighbors(user_item_matrix.iloc[user_index].values.reshape(1, -1))

    # Get the indices of the most similar users (excluding the user itself)
    similar_users = indices.flatten()[1:]  # Exclude the user itself (index 0)

    # Aggregate the ratings of the similar users
    aggregated_ratings = np.mean(user_item_matrix.iloc[similar_users], axis=0)

    # Get the top N items based on aggregated ratings
    top_items = np.argsort(aggregated_ratings)[::-1][:top_n]

    return top_items

# Accept the userId as a command-line argument
if len(sys.argv) > 1:
    try:
        userId = sys.argv[1]  # Keep userId as a string
        # Ensure the userId exists in the dataset
        if userId not in df['userId'].astype(str).values:
            raise ValueError("Invalid userId")
        user_index = df[df['userId'] == userId].index[0]  # Get the index of the user in the matrix
        recommended_items = recommend_items(user_index, user_item_matrix, knn, top_n=6)

        # Map item indices to item names
        item_names = df.columns[1:]  # Assuming the first column is the userId
        recommended_item_names = [item_names[i] for i in recommended_items]

        # Ensure that the output is a valid UTF-8 encoded JSON
        result = json.dumps({'recommendedItems': recommended_item_names}, ensure_ascii=False)  # ensure_ascii=False to support non-ASCII characters
        sys.stdout.buffer.write(result.encode('utf-8'))  # Output the result as UTF-8 encoded bytes

    except Exception as e:
        result = json.dumps({'error': str(e)}, ensure_ascii=False)
        sys.stdout.buffer.write(result.encode('utf-8'))  # Ensure error messages are encoded correctly
else:
    result = json.dumps({'error': 'No userId provided'}, ensure_ascii=False)
    sys.stdout.buffer.write(result.encode('utf-8'))  # Ensure error messages are encoded correctly
