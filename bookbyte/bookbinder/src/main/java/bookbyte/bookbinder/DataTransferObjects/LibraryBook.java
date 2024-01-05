package bookbyte.bookbinder.DataTransferObjects;

import java.util.UUID;

public class LibraryBook {

    private LibraryBook() {

    }

    public static class LibraryBookCreationRequest {
        private String isbn13;
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsbn13() {
            return isbn13;
        }

        public void setIsbn13(String isbn13) {
            this.isbn13 = isbn13;
        }
    }

    public static class LibraryBookLendingRequest {
        private String id;
        private UUID uuid;

        private int days;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public int getDays() {
            return days;
        }

        public void setDays(int days) {
            this.days = days;
        }

    }
}
