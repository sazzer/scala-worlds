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


    
Google+
-------
Google+ Authentication works using OAuth 2, documented [Here](https://developers.google.com/+/web/signin/server-side-flow.)

Facebook
--------
Facebook authentication uses their own custom Javascript SDK, or else you can do it yourself using their own routines. This 
does not conform to OAuth or OpenID. The Javascript SDK is documented [Here](https://developers.facebook.com/docs/javascript), or
else the mechanism to do it without is documented [Here](https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow/v2.0)


