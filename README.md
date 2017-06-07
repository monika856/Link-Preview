Link Preview App - 
It is an Android app that helps to save URLs. It stores and displays the preview - link, title, description and an image for saved URLs.
The app has a screen with an input box and a submit button to fetch and save the preview and a 'Feeling Lucky' button which takes a URL from some random (hardcoded) URLs.
The Get Data Intent service fetches the URL and gets all the details from HTML DOM. It looks for the meta attributes first and falls backs on the default title, description, and first image of the web page if they are not available.

Technical Choices taken / Libraries Included- 
1. Android Support Library and it's components for backward compatible material design UI.
2. JSoup - For extracting and manipulating data from the HTML Web page, using DOM traversal, CSS.
3. Glide - I have used Glide for loading and displaying images.It uses memory and disk caching by default to avoid unnecessary   network requests.
4. GSON - For JSON serialization and deserialization. ( Used to quickly, easily store the saved links in shared preferences as   a JSON string).
5. Event Bus - For synchronous and asynchronous inter-component communication between Android components using Pub/Sub pattern.

More that could be done -
1. Filtering, Sorting, Searching based on date added, site title/ domain name. 
2. Remove/Read/Archive Functionality.
3. In App Link open - Open the link in the app itself using web view so that user does not have to leave the app.
