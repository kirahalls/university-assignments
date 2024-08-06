/*
 * friendlist.c - [Starting code for] a web-based friend-graph manager.
 *
 * Based on:
 *  tiny.c - A simple, iterative HTTP/1.0 Web server that uses the 
 *      GET method to serve static and dynamic content.
 *   Tiny Web server
 *   Dave O'Hallaron
 *   Carnegie Mellon University
 */
#include "csapp.h"
#include "dictionary.h"
#include "more_string.h"

static void doit(int fd);
void* handle_client(void* arg);
static dictionary_t *read_requesthdrs(rio_t *rp);
static void read_postquery(rio_t *rp, dictionary_t *headers, dictionary_t *d);
static void clienterror(int fd, char *cause, char *errnum, 
                        char *shortmsg, char *longmsg);
static void print_stringdictionary(dictionary_t *d);
static void serve_request(int fd, dictionary_t *query);
static void serve_greet(int fd, dictionary_t *query);
static void serve_friends(int fd, dictionary_t *query);
static void serve_befriend(int fd, dictionary_t *query);
static void serve_unfriend(int fd, dictionary_t *query);
static void serve_introduce(int fd, dictionary_t *query);

dictionary_t* friends;
static pthread_mutex_t mutex;


int main(int argc, char **argv) 
{
  int listenfd, connfd;
  char hostname[MAXLINE], port[MAXLINE];
  socklen_t clientlen;
  struct sockaddr_storage clientaddr;
  pthread_t tid;
  int* fd;

  friends = make_dictionary(0, (void*) free_dictionary);
  pthread_mutex_init(&mutex, NULL);

  /* Check command line args */
  if (argc != 2) {
    fprintf(stderr, "usage: %s <port>\n", argv[0]);
    exit(1);
  }

  listenfd = Open_listenfd(argv[1]);

  /* Don't kill the server if there's an error, because
     we want to survive errors due to a client. But we
     do want to report errors. */
  exit_on_error(0);

  /* Also, don't stop on broken connections: */
  Signal(SIGPIPE, SIG_IGN);

  while (1) {

    clientlen = sizeof(clientaddr);
    connfd = Accept(listenfd, (SA *)&clientaddr, &clientlen);
    if (connfd >= 0) {
      Getnameinfo((SA *) &clientaddr, clientlen, hostname, MAXLINE, 
                  port, MAXLINE, 0);
      printf("Accepted connection from (%s, %s)\n", hostname, port);
     
      fd = (int*) malloc(sizeof(int));
      *(int*) fd = connfd;
      Pthread_create(&tid, NULL, handle_client, fd);
      Pthread_detach(tid);
    }
    else 
    {
      printf("Connection failed from main");
    }

  }
  
  free_dictionary(friends);
  
  pthread_mutex_destroy(&mutex);
}

void* handle_client(void* arg)
{
  int fd = *(int*) arg;
  doit(fd);
  Close(fd);
  free(arg); 
  return 0;
}

/*
 * doit - handle one HTTP request/response transaction
 */
void doit(int fd) 
{
  char buf[MAXLINE], *method, *uri, *version;
  rio_t rio;
  dictionary_t *headers, *query;

  /* Read request line and headers */
  Rio_readinitb(&rio, fd);
  if (Rio_readlineb(&rio, buf, MAXLINE) <= 0) {
    Close(fd);
    return;
  }
    
  printf("%s", buf);
  
  if (!parse_request_line(buf, &method, &uri, &version)) {
    clienterror(fd, method, "400", "Bad Request",
                "Friendlist did not recognize the request");
  } else {
    //Print URI as a sanity check
    printf("%s %s", "URI: ", uri);
    if (strcasecmp(version, "HTTP/1.0")
        && strcasecmp(version, "HTTP/1.1")) {
      clienterror(fd, version, "501", "Not Implemented",
                  "Friendlist does not implement that version");
    } else if (strcasecmp(method, "GET")
               && strcasecmp(method, "POST")) {
      clienterror(fd, method, "501", "Not Implemented",
                  "Friendlist does not implement that method");
    } else {
      headers = read_requesthdrs(&rio);
      
      

      /* Parse all query arguments into a dictionary */
      query = make_dictionary(COMPARE_CASE_SENS, free);
      parse_uriquery(uri, query);
      if (!strcasecmp(method, "POST"))
        read_postquery(&rio, headers, query);

      /* For debugging, print the dictionary */
      print_stringdictionary(query);

      /* You'll want to handle different queries here,
         but the intial implementation always returns
         nothing: */
      if (starts_with("/greet", uri))
      {
        serve_greet(fd, query); 
      }
      else if (starts_with("/friends", uri))
      {
        serve_friends(fd, query);
      }
      else if (starts_with("/befriend", uri))
      {
        serve_befriend(fd, query);
      }
      else if (starts_with("/unfriend", uri))
      {
        serve_unfriend(fd, query);
      }
      else if (starts_with("/introduce", uri))
      {
        printf("%s", "calling introduce function\n");
        serve_introduce(fd, query);
      }
      else 
      {
        serve_request(fd, query);
      }

      /* Clean up */
      free_dictionary(query);
      free_dictionary(headers);
    }

    /* Clean up status line */
    free(method);
    free(uri);
    free(version);
    
  }
  
}

/*
 * read_requesthdrs - read HTTP request headers
 */
dictionary_t *read_requesthdrs(rio_t *rp) 
{
  //printf("%s", "reading request headers\n");
  char buf[MAXLINE];
  dictionary_t *d = make_dictionary(COMPARE_CASE_INSENS, free);

  Rio_readlineb(rp, buf, MAXLINE);
  printf("%s", buf);
  while(strcmp(buf, "\r\n")) {
    Rio_readlineb(rp, buf, MAXLINE);
    printf("%s", buf);
    parse_header_line(buf, d);
  }
  
  return d;
}

void read_postquery(rio_t *rp, dictionary_t *headers, dictionary_t *dest)
{
  printf("%s", "reading post query\n");
  char *len_str, *type, *buffer;
  int len;
  
  len_str = dictionary_get(headers, "Content-Length");
  len = (len_str ? atoi(len_str) : 0);

  type = dictionary_get(headers, "Content-Type");
  
  buffer = malloc(len+1);
  Rio_readnb(rp, buffer, len);
  buffer[len] = 0;

  if (!strcasecmp(type, "application/x-www-form-urlencoded")) {
    parse_query(buffer, dest);
  }

  free(buffer);
}

static char *ok_header(size_t len, const char *content_type) {
  char *len_str, *header;
  
  header = append_strings("HTTP/1.0 200 OK\r\n",
                          "Server: Friendlist Web Server\r\n",
                          "Connection: close\r\n",
                          "Content-length: ", len_str = to_string(len), "\r\n",
                          "Content-type: ", content_type, "\r\n\r\n",
                          NULL);
  free(len_str);

  return header;
}

/*
 * serve_request - example request handler
 */
static void serve_request(int fd, dictionary_t *query)
{
  size_t len;
  char *body, *header;

  body = strdup("alice\nbob");

  len = strlen(body);

  /* Send response headers to client */
  header = ok_header(len, "text/html; charset=utf-8");
  Rio_writen(fd, header, strlen(header));
  printf("Response headers:\n");
  printf("%s", header);

  free(header);

  /* Send response body to client */
  Rio_writen(fd, body, len);

  free(body);
}

/*
* serve_greet - greets the user by writing a greeting alongside their name
*/
static void serve_greet(int fd, dictionary_t *query)
{
  size_t len;
  char *body, *header;

  char* user = dictionary_get(query, "user");
  body = append_strings("Greetings, ", user, "!", NULL);
  

  len = strlen(body);

  /* Send response headers to client */
  header = ok_header(len, "text/plain; charset=utf-8");
  Rio_writen(fd, header, strlen(header));
  printf("Response headers:\n");
  printf("%s", header);

  free(header);

  /* Send response body to client */
  Rio_writen(fd, body, len);

  free(body);
}


/*
* serve_friends - prints out the friends of the user specified in the query, printing nothing if the user 
* has no friends.
*/
static void serve_friends(int fd, dictionary_t *query)
{
  size_t len;
  char *body, *header;

  pthread_mutex_lock(&mutex);
  char* user = dictionary_get(query, "user");
  dictionary_t* user_friends = dictionary_get(friends, user);
  body = "";

  if (user_friends == NULL)
  {
    body = append_strings(body, NULL);
  }
  else 
  {
    //Print friend list
    const char** friend_list = dictionary_keys(user_friends);
    char* friend_array = join_strings(friend_list, (char) '\n');
  
    printf("%s\n", friend_array);
    body = append_strings(body, friend_array, NULL);
  }
  pthread_mutex_unlock(&mutex);
  

  len = strlen(body);

  /* Send response headers to client */
  header = ok_header(len, "text/plain; charset=utf-8");
  Rio_writen(fd, header, strlen(header));
  printf("Response headers:\n");
  printf("%s", header);

  free(header);
  /* Send response body to client */
  Rio_writen(fd, body, len);
  

  free(body);
}


/*
* serve_befriend - Takes in a friend or a list of friends in the query and adds each user in that list as a friend of the user
* specified in the query. This implies each person in the friend list will also add the user as a friend. After 
* the friends have been added, the list of the user's friends is printed, similar to the '/friends' query.
*/
static void serve_befriend(int fd, dictionary_t *query)
{
  size_t len;
  char *body, *header;
 
  pthread_mutex_lock(&mutex);
  char* user = dictionary_get(query, "user");
  char*  new_friends = dictionary_get(query, "friends");
  char** list_of_friends = split_string(new_friends, '\n');
  dictionary_t* user_friends = dictionary_get(friends, user);
  
  
  // Add the friend to the user's friend list 
  if (user_friends == NULL)
  {
   
    // Set the value of the 'user' key in the dictionary to the new empty dictionary
    dictionary_set(friends, user, make_dictionary(0, NULL));
    user_friends = dictionary_get(friends, user);
    //Add friends to the dictionary
    int i = 0;
    char* current_string = list_of_friends[i];
    while (current_string != NULL)
    {
      //Only set them as a friend if they are not the user themselves
      if (strcmp(current_string, user))
      {
        dictionary_set(user_friends, current_string, NULL); 
      }
      i++;
      current_string = list_of_friends[i];
    } 
  }
  else 
  {
    int i = 0;
    char* current_string = list_of_friends[i];
    while (current_string != NULL)
    {
      //Only set them as a friend if they are not the user themselves
      if (strcmp(current_string, user))
      {
        dictionary_set(user_friends, current_string, NULL); 
      }
      i++;
      current_string = list_of_friends[i];
    } 
  }
  user_friends = dictionary_get(friends, user);

  //Add the user to the friends' friend list
  dictionary_t* current_dictionary;
  int i = 0;
  char* current_friend = list_of_friends[i];
  while (current_friend != NULL)
  {
    current_dictionary = dictionary_get(friends, current_friend);
    if (current_dictionary == NULL)
    {
      // Set the value of the 'user' key in the dictionary to the new empty dictionary
      //Only set them as a friend if they are not the user themselves
      if (strcmp(current_friend, user))
      {
        dictionary_set(friends, current_friend, make_dictionary(0, NULL));
        current_dictionary = dictionary_get(friends, current_friend);
        dictionary_set(current_dictionary, user, NULL);
      }
      
    }
    else 
    {
      //Only set them as a friend if they are not the user themselves
      if (strcmp(current_friend, user))
      {
        dictionary_set(current_dictionary, user, NULL);
      }
      
    }

    i++;
    current_friend = list_of_friends[i];
  }
  

  //Print friend list
  const char** friend_list = dictionary_keys(user_friends);
  char* friend_array = join_strings(friend_list, (char) '\n');
  body = "";
  body = append_strings(body, friend_array, NULL);

  pthread_mutex_unlock(&mutex);

  len = strlen(body);

  /* Send response headers to client */
  header = ok_header(len, "text/plain; charset=utf-8");
  Rio_writen(fd, header, strlen(header));
  printf("Response headers:\n");
  printf("%s", header);

  free(header);

  /* Send response body to client */
  Rio_writen(fd, body, len);
  
  
  free(body);
}


/*
* serve_unfriend - Takes in a friend or list of friends and a user. Each friend in the list of friends 
* should be removed from the user's list of friends. This also implies the user will be removed as a friend 
* from each friend on the friend list. Once the removal has been completed, it will print out the updated list 
* of the user's friends.
*/
static void serve_unfriend(int fd, dictionary_t *query)
{
   size_t len;
  char *body, *header;

  pthread_mutex_lock(&mutex);
  char* user = dictionary_get(query, "user");
  char*  new_unfriends = dictionary_get(query, "friends");
  char** list_of_unfriends = split_string(new_unfriends, '\n');
  dictionary_t* user_friends = dictionary_get(friends, user);
  
  
  // Add the friend to the user's friend list 
  if (!(user_friends == NULL))
  {
    
    int i = 0;
    char* current_string = list_of_unfriends[i];
    while (current_string != NULL)
    {
      dictionary_remove(user_friends, current_string); 
      i++;
      current_string = list_of_unfriends[i];
    } 
  }
  user_friends = dictionary_get(friends, user);

  //Add the user to the friends' friend list
  dictionary_t* current_dictionary;
  int i = 0;
  char* current_friend = list_of_unfriends[i];
  while (current_friend != NULL)
  {
    current_dictionary = dictionary_get(friends, current_friend);
    if (current_dictionary == NULL)
    {
    }
    else 
    {
      dictionary_remove(current_dictionary, user);
    }

    i++;
    current_friend = list_of_unfriends[i];
  }
  

  //Print friend list
  const char** friend_list = dictionary_keys(dictionary_get(friends, user));
  char* friend_array = join_strings(friend_list, (char) '\n');
  body = "";
  body = append_strings(body, friend_array, NULL);

  pthread_mutex_unlock(&mutex);

  len = strlen(body);

  /* Send response headers to client */
  header = ok_header(len, "text/plain; charset=utf-8");
  Rio_writen(fd, header, strlen(header));
  printf("Response headers:\n");
  printf("%s", header);

  free(header);

  /* Send response body to client */
  Rio_writen(fd, body, len);

  
  free(body);
}


/*
* serve_introduce - Takes in a friend-list server, a friend name, and a user name. This server will contact the 
* parameter server and get the list of friends of the friend parameter, and add the friend and the list of friends
* as friends of the user. This also implies the user will be added as a friend to each of those friends.
*/
static void serve_introduce(int fd, dictionary_t *query)
{
  size_t len;
  char *body, *header, *request;

  pthread_mutex_lock(&mutex);
  char* user = dictionary_get(query, "user");
  char* friend = dictionary_get(query, "friend");
  char* host = dictionary_get(query, "host");
  char* port = dictionary_get(query, "port");

  pthread_mutex_unlock(&mutex);

  body = "";
  len = strlen(body);
 
  

  int clientfd;
  rio_t rio;
  char response_buf[MAXLINE];

  //Connect to other server
  clientfd = open_clientfd(host, port);
  rio_readinitb(&rio, clientfd);
  printf("%s, %d\n", "opened client ", clientfd);
 
 

  request = "";
  // Construct the HTTP GET request
  request = append_strings("GET /friends?user=", friend,  " ", 
      "HTTP/1.1\r\nHost:", host, ":", port, "\r\n"
      "Connection: close\r\n\r\n" );
  // Send the request
  Rio_writen(clientfd, (void*) request, strlen(request));
 
  dictionary_t* friends_dictionary;
    
  //Get response header from other server 
  ssize_t num_read;

  char* current_friend;
  
  while ((num_read = Rio_readlineb(&rio, response_buf, MAXLINE)) > 0)
  {
    //break out of loop once we reach the end of the header
    if (starts_with("\r\n", response_buf))
    {
      break;
    }
  }
  pthread_mutex_lock(&mutex);
  dictionary_t* user_friends = dictionary_get(friends, user);
  if (user_friends == NULL)
  {
    // Set the value of the 'user' key in the dictionary to the new empty dictionary
    dictionary_set(friends, user,  make_dictionary(0, NULL));
    user_friends = dictionary_get(friends, user);
  }
 
  while ((num_read = Rio_readlineb(&rio, response_buf, MAXLINE)) > 0 )
  {
    printf("%ld\n", num_read);

    //Skip any whitespace lines
    if (! starts_with("\n", response_buf))
    {
      current_friend = (char*) response_buf;

      //Remove \n terminator at end of string to avoid formatting issues
      current_friend[strlen(current_friend) -1] = '\0';
      
      //Only add them as a friend if it isn't the user themselves
      if (strcmp(user, current_friend))
      {
        
        //Set user to have the current_friend as part of their friend list
        dictionary_set(user_friends, current_friend, NULL);

        //Set current_friend to have user as part of their friend list
        friends_dictionary = dictionary_get(friends, current_friend);
      
        if (friends_dictionary == NULL)
        {

          // Set the value of the 'user' key in the dictionary to the new empty dictionary
          dictionary_set(friends, current_friend, make_dictionary(0, NULL));
          friends_dictionary = dictionary_get(friends, current_friend);
        }
        dictionary_set(friends_dictionary, user, NULL);
      }
      else 
      {
        break;
        
      }
    }  
       
  }
  pthread_mutex_unlock(&mutex);

  /* Send response headers to client */
  header = ok_header(len, "text/plain; charset=utf-8");
  Rio_writen(fd, header, strlen(header));
  printf("Response headers:\n");
  printf("%s", header);

  free(header);

  /* Send response body to client */
  Rio_writen(fd, body, len);  
}

/*
 * clienterror - returns an error message to the client
 */
void clienterror(int fd, char *cause, char *errnum, 
		 char *shortmsg, char *longmsg) 
{
  size_t len;
  char *header, *body, *len_str;

  body = append_strings("<html><title>Friendlist Error</title>",
                        "<body bgcolor=""ffffff"">\r\n",
                        errnum, " ", shortmsg,
                        "<p>", longmsg, ": ", cause,
                        "<hr><em>Friendlist Server</em>\r\n",
                        NULL);
  len = strlen(body);

  /* Print the HTTP response */
  header = append_strings("HTTP/1.0 ", errnum, " ", shortmsg, "\r\n",
                          "Content-type: text/html; charset=utf-8\r\n",
                          "Content-length: ", len_str = to_string(len), "\r\n\r\n",
                          NULL);
  free(len_str);
  
  Rio_writen(fd, header, strlen(header));
  Rio_writen(fd, body, len);

  free(header);
  free(body);
}

static void print_stringdictionary(dictionary_t *d)
{
  int i, count;

  count = dictionary_count(d);
  for (i = 0; i < count; i++) {
    printf("%s=%s\n",
           dictionary_key(d, i),
           (const char *)dictionary_value(d, i));
  }
  printf("\n");
}
