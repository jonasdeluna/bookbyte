function generateRandomString(length: number) {
  var characters =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  var result = "";
  var charactersLength = characters.length;

  for (var i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * charactersLength));
  }

  return result;
}

function generateRandomNumberString(length: number) {
  var characters = "0123456789";
  var result = "";
  var charactersLength = characters.length;

  for (var i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * charactersLength));
  }

  return result;
}

describe("Visit bookbyte page", () => {
  before(() => {
    // Assuming backend is already running and accessible at this URL
    cy.visit("http://localhost:8080");
  });

  it("Visits bookbyte and checks for 'Add book' text", () => {
    // Visit the main page of the app
    const id = generateRandomNumberString(16);
    const isbn = generateRandomNumberString(13);
    const title = generateRandomString(10);
    const author = generateRandomString(10);
    cy.visit("http://localhost:3000");

    // Check if the text 'Add book' is present in the DOM
    cy.contains("Add book");
    cy.get("button").contains("Add book").click();
    cy.contains("Add book to library");
    cy.get('[name="id"]').focus().type(id);
    cy.get('[name="isbn13"]').focus().type(isbn);
    cy.get('[name="title"]').focus().type(title);
    cy.get('[name="author"]').focus().type(author);
    cy.get("[type=submit]").click();
    cy.contains(id);
    cy.contains(title);
    cy.contains(author);
  });

  after(() => {});
});
