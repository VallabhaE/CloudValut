package main

import (
	"database/sql"
	"encoding/base64"
	"errors"
)

func GetDb() (*sql.DB, error) {
	dsn := "root:password@tcp(localhost:3307)/CLOUDVAULT?parseTime=true"
	db, err := sql.Open("mysql", dsn)
	if err != nil {
		return nil, err
	}

	if err := db.Ping(); err != nil {
		return nil, err
	}

	return db, nil
}

func ValidateUser(db *sql.DB, name string, encodedPassword string) (*User, error) {

	var user User

	query := `
		SELECT id, email, name, created_at, pass
		FROM users
		WHERE name = ?
	`

	err := db.QueryRow(query, name).Scan(
		&user.ID,
		&user.Email,
		&user.Name,
		&user.CreatedAt,
		&user.Pass,
	)

	if err != nil {
		return nil, err
	}

	decodedPassword, err := base64.StdEncoding.DecodeString(encodedPassword)
	if err != nil {
		return nil, err
	}

	if string(decodedPassword) != user.Pass {
		return nil, errors.New("invalid password")
	}

	return &user, nil
}

func CreateUser(db *sql.DB, email string, name string, password string) (*User, error) {

	query := `
		INSERT INTO users (email, name, pass)
		VALUES (?, ?, ?)
	`

	result, err := db.Exec(query, email, name, password)
	if err != nil {
		return nil, err
	}

	id, err := result.LastInsertId()
	if err != nil {
		return nil, err
	}

	return ValidateUser(db, email, password)
}
