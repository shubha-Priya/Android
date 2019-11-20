public class MainActivity extends AsyncTask<String, Void, String> {

                private Context context;
                Boolean error, success;

                public SignupActivity(Context context) {
                    this.context = context;
                }

                protected void onPreExecute() {

                }

                @Override
                protected String doInBackground(String... arg0) {
                    String fullName = arg0[0];
                  //  String userName = arg0[1];
                    String passWord = arg0[1];
                    String phoneNumber = arg0[2];
                    String emailAddress = arg0[3];

                    String link;
                    String data;
                    BufferedReader bufferedReader;
                    String result;

                    try {
                        data = "?fullname=" + URLEncoder.encode(fullName, "UTF-8");
                    //    data += "&username=" + URLEncoder.encode(userName, "UTF-8");
                        data += "&password=" + URLEncoder.encode(passWord, "UTF-8");
                        data += "&phonenumber=" + URLEncoder.encode(phoneNumber, "UTF-8");
                        data += "&emailaddress=" + URLEncoder.encode(emailAddress, "UTF-8");

                    link = "http://xyz/reg/tryrr.php" + data;

                        URL url = new URL(link);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();

                        bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        result = bufferedReader.readLine();
                        return result;

                    } catch (Exception e) {
                     // return new String("Exception: " + e.getMessage());
                       // return null;
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(String result) {

                    String jsonStr = result;

                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        String query_result = jsonObj.getString("query_result");

                        if (query_result.equals("SUCCESS")) {
                            Toast.makeText(context, "Data inserted successfully. Signup successfully.", Toast.LENGTH_LONG).show();

                        }
                        else if (query_result.equals("FAILURE")) {
                            Toast.makeText(context, "Data could not be inserted, fill all records.", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
            }

            //php file

            <?php
            // array for JSON response
            $response = array();
            // include db connect class
            //require_once __DIR__ . '/db_connect.php';
            $con=mysqli_connect("localhost","user","password","db_name");

            // connecting to db
            //$db = new DB_CONNECT();

            // check for required fields
            if (isset($_GET['fullname']) && isset($_GET['password']) && isset($_GET['phonenumber']) && isset($_GET['emailaddress'])) {

            $fullName = $_GET['fullname'];
            //$userName = $_GET['username'];
            $passWord = $_GET['password'];
            $phoneNumber = $_GET['phonenumber'];
            $emailAddress = $_GET['emailaddress'];

            // mysql inserting a new row
            $result = mysql_query("INSERT INTO users10 (fullname,password,phone,email)
            SELECT * FROM (SELECT '$fullName', '$passWord', '$phoneNumber','$emailAddress') AS tmp
            WHERE NOT EXISTS (
                SELECT phone FROM users10 WHERE phone = '$phoneNumber' 
            )");

            if ($result) {
                // successfully inserted into database
                $response["success"] = 1;
                $response["message"] = "You are successfully registered to MEMS.";

                // echoing JSON response
                echo json_encode($response);
            } 
            else {
                // failed to insert row
                $response["success"] = 0;
                $response["message"] = "User Exist";

                // echoing JSON response
                echo json_encode($response);
            }
            } else {
            // required field is missing
            $response["success"] = 0;
            $response["message"] = "Required field(s) is missing";

            // echoing JSON response
            echo json_encode($response);
            }
?>