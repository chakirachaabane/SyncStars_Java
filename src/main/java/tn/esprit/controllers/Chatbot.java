package tn.esprit.controllers;

import java.util.ArrayList;
import java.util.List;

public class Chatbot {

    public String getWelcomeMessage() {
        return "Bienvenue ! Comment puis-je vous aider ?";
    }

    public String respondToMessage(String message) {
        // Ajoutez votre logique de chatbot ici
        return "Je suis un chatbot, je ne peux pas encore répondre à cela.";
    }

    public String respondToQuestion(String question) {
        // Définissez ici les réponses aux questions prédéfinies
        switch (question) {
            case "Quels sont vos horaires d'ouverture ?":
                return "Nous sommes ouverts du lundi au vendredi de 9h à 18h.";
            case "Comment puis-je prendre rendez-vous ?":
                return "Vous pouvez prendre rendez-vous en utilisant notre application ou en nous appelant.";
            case "Quels services proposez-vous ?":
                return "Nous proposons une gamme de services, y compris la consultation, les examens médicaux, etc.";
            default:
                return "Désolé, je ne comprends pas votre question.";
        }
    }

    public List<String> getPredefinedQuestions() {
        // Retourne une liste de questions prédéfinies
        List<String> predefinedQuestions = new ArrayList<>();
        predefinedQuestions.add("Quels sont vos horaires d'ouverture ?");
        predefinedQuestions.add("Comment puis-je prendre rendez-vous ?");
        predefinedQuestions.add("Quels services proposez-vous ?");
        // Ajoutez d'autres questions prédéfinies si nécessaire
        return predefinedQuestions;
    }
}
