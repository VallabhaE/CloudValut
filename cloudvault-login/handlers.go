package main

import (
	"encoding/base64"
	"encoding/json"
	"net/http"
)

func LoginHandler(w http.ResponseWriter, r *http.Request) {

	db, err := GetDb()
	if err != nil {
		http.Error(w, "database error", http.StatusInternalServerError)
		return
	}
	defer db.Close()

	var req LoginRequest

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "invalid request", http.StatusBadRequest)
		return
	}

	decoded, err := base64.StdEncoding.DecodeString(req.Password)
	if err != nil {
		http.Error(w, "invalid password encoding", http.StatusBadRequest)
		return
	}

	user, err := ValidateUser(db, req.Email, string(decoded))
	if err != nil {
		http.Error(w, "invalid user", http.StatusUnauthorized)
		return
	}

	// Create JWT
	token, err := createToken(req.Email, user.ID)
	if err != nil {
		http.Error(w, "could not create token", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")

	json.NewEncoder(w).Encode(map[string]interface{}{
		"status": "ok",
		"token":  token,
		"user": map[string]interface{}{
			"id":    user.ID,
			"name":  user.Name,
			"email": user.Email,
		},
	})
}

func SignupHandler(w http.ResponseWriter, r *http.Request) {
	db, err := GetDb()
	if err != nil {
		http.Error(w, "database error", http.StatusInternalServerError)
		return
	}
	defer db.Close()

	var req SignupRequest

	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "invalid request", http.StatusBadRequest)
		return
	}

	if req.Email == "" || req.Name == "" || req.Password == "" {
		http.Error(w, "email, name and password are required", http.StatusBadRequest)
		return
	}

	password, err := base64.StdEncoding.DecodeString(req.Password)
	if err != nil {
		http.Error(w, "invalid password encoding", http.StatusBadRequest)
		return
	}

	user, err := CreateUser(
		db,
		req.Email,
		req.Name,
		string(password),
	)

	if err != nil {
		http.Error(w, "could not create user", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)

	json.NewEncoder(w).Encode(map[string]interface{}{
		"id":    user.ID,
		"name":  user.Name,
		"email": user.Email,
	})
}
