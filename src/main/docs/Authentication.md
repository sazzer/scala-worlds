Authentication
==============

Twitter
-------
Twitter authentication works using OAuth, documented [Here](https://dev.twitter.com/docs/auth/obtaining-access-tokens.)

1. Obtaining a request token
    Send a Signed POST request from the server to **oauth/request\_token**. This must include the *oauth\_callback* parameter
    in addition to all of the normal OAuth parameters. The response to this will contain the *oauth\_token* and *oauth\_token\_secret*
    that represent the Request Token
    
2. Redirect the user
    Redirect the user to **oauth/authenticate?oauth\_token=...** (Or **oauth/authorize?oauth\_token=...** if we want to be able to post).
    On success, the user will be sent back to the *oauth\_callback* URL, with an *oauth\_token* and *oauth\_verifier* parameter
    
3. Obtain an access token
    Send a Signed POST request from the server to **oauth/access\_token**. This must include the *oauth\_verifier* value as a payload
    and the *oauth\_token* as an OAuth parameter in addition to all of the normal OAuth parameters. The response to this will contain
    the *oauth\_token* and *oauth\_token\_secret* that represent the Access Token. These should then be stored and used for all future
    requests to the Twitter API on behalf of this user
    
4. (Optional) Verify the credentials of the user
    Send a Signed GET request from the server to **account/verify\_credentials**. The response includes the account details, including
    but not limited to: Real Name, Screen Name, Location, Language, Time Zone.
    
At this point, the user is signed in and we know enough about them to proceed.
    
### Signing a request
All API calls from the Server to the Twitter API need to be signed according to the OAuth profile. This is achieved by adding an
*Authorization* header to the HTTP Request that contains all of the signing details for that request. Details on how this is achieved
can be found [Here](https://dev.twitter.com/docs/auth/authorizing-request)

The OAuth Authorization header consists of a number of key/value pairs, as follows:

* *oauth_callback* - This is only ever present on the request for a Request Token, described above.
* *oauth_consumer_key* - This is the Consumer Key from the Twitter Application configuration
* *oauth_nonce* - This is a randomly generated string that should be unique. Use UUID.randomUUID().toString()
* *oauth_signature* - This is generated from all of the other parameters, the request itself, and some shared secrets. See below
* *oauth_signature_method* - This is always "HMAC-SHA1"
* *oauth_timestamp* - This is the seconds since the epoch for the request
* *oauth_token* - This is the Access Token we are using for the request, if we have one. This might not be present
* *oauth_version* - This is always "1.0"

### Generating the Signature
In order to generate the value for *oauth_signature*, the following values are required:

* HTTP Method
* The full HTTP URL being called, including the schema but not any Query String parameters
* All Query String and Payload Parameters
* Every *oauth_...* parameter from above Except for *oauth_signature* as we've not generated that yet
* The Consumer Secret from the Twitter Application Configuration
* The (Optional) Access Token Secret.

The actual process to generate the signature is as follows

1. Sort all of the Query String, Payload and OAuth parameters together in alphabetical order on the key name
2. Percent encode both the key and the value of all of these
3. Generate a string that is "<key>=<value>", separated by "&" characters. Do not append an & on the end, only between values
4. Generate a string that is built from the following, separated by "&" characters:
    1. The HTTP Method, Percent Encoded
    2. The HTTP URL, Percent Encoded
    3. The already generated parameter string, Percent Encoded (again)
5. Generate the Signing key by appending the Percent Encoded Consumer Secret, an "&" character, and then if present the Percent Encoded Access Token Secret
6. Pass these two strings through the HMAC-SHA1 hashing algorithm, and Base64 encode the resulting hash

#### Building the header string

1. Append the string "OAuth "
2. For each of the above that are present, in alphabetical order of the key name
    1. Append the key name, percent encoded
    2. Append the string "=""
    3. Append the value, percent encoded
    4. Append the string """
    5. If this is not the last value, append the string ", "

    
Google+
-------
Google+ Authentication works using OAuth 2, documented [Here](https://developers.google.com/+/web/signin/server-side-flow.)

Facebook
--------
Facebook authentication uses their own custom Javascript SDK, or else you can do it yourself using their own routines. This 
does not conform to OAuth or OpenID. The Javascript SDK is documented [Here](https://developers.facebook.com/docs/javascript), or
else the mechanism to do it without is documented [Here](https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow/v2.0)


