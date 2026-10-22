package lk.educore.schoolms.model;

import lk.educore.schoolms.exceptions.BusinessException;
import lk.educore.schoolms.exceptions.ValidationException;

import java.util.Objects;

/**
 * Book Domain Model
 * Represents a library book in EduCore.
 *
 * @author EduCore Software Team
 * @version 1.0.0
 */
public class Book {

    private int bookId;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private int totalCopies;
    private int availableCopies;

    public Book() {
    }

    public Book(int bookId, String title, String author, String isbn, String category, int totalCopies)
            throws ValidationException {
        if (totalCopies < 0) {
            throw new ValidationException("Total copies cannot be negative", "totalCopies", String.valueOf(totalCopies));
        }
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public boolean isAvailable() {
        return availableCopies > 0;
    }

    public void borrowCopy() throws BusinessException {
        if (!isAvailable()) {
            throw new BusinessException("No copies available for borrowing: " + title,
                    BusinessException.ErrorType.OPERATION_NOT_ALLOWED, "Book", String.valueOf(bookId));
        }
        availableCopies--;
    }

    public void returnCopy() throws BusinessException {
        if (availableCopies >= totalCopies) {
            throw new BusinessException("All copies of this book are already in the library",
                    BusinessException.ErrorType.BUSINESS_RULE_VIOLATION, "Book", String.valueOf(bookId));
        }
        availableCopies++;
    }

    // Getters and Setters
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookId == book.bookId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId);
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", available=" + availableCopies + "/" + totalCopies +
                '}';
    }
}
