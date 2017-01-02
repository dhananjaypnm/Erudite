<?php

	$servername = "localhost";
	$dbusername = "root";
	$dbpassword = "notMySequel";
	$dbname = "rest_api";
	


	try {
		    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $dbusername, $dbpassword);
				// set the PDO error mode to exception
				$conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
				$conn->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
			
				

				if($_SERVER['REQUEST_METHOD']=='POST'){
					

				
					$request_code=$_POST['request_code'];
				

			    	if($request_code==1){
						
						//registration
					
							$sign_up_statement=$conn->prepare("INSERT INTO `user_info_table`(`unique_id`, `name`, `user_name`, `password`, `phone`, `public_key`, `private_key`,`no_of_monitors`, `ids_of_monitors`) VALUES (DEFAULT,:name,:user_name,:password,:phone,:public_key,:private_key,:no_of_monitors,:ids_of_monitors)");
						$sign_up_statement->bindParam(':name',$name);
						$sign_up_statement->bindParam(':user_name',$user_name);
						$sign_up_statement->bindParam(':password',$password);
						$sign_up_statement->bindParam(':phone',$phone);
						$sign_up_statement->bindParam(':public_key',$public_key);
						$sign_up_statement->bindParam(':private_key',$private_key);
						$sign_up_statement->bindParam(':no_of_monitors',$no_of_monitors);
						$sign_up_statement->bindParam(':ids_of_monitors',$ids_of_monitors);

						$name=$_POST['name'];
						$user_name=$_POST['user_name'];
						$password=$_POST['password'];
						$phone=$_POST['phone'];
						$public_key=$_POST['public_key'];
						$private_key=$_POST['private_key'];
						$no_of_monitors=0;
						$ids_of_monitors="";


						$sign_up_statement->execute();

					
		 
												
						$sign_up_result=array();
						$sign_up_result['success']=1;
						$sign_up_result['message']='records inserted';
						echo json_encode($sign_up_result);
			 
					}   else if($request_code==2){
						//loginAuth

						$user_name=$_POST['user_name'];
						$password=$_POST['password'];


						$login_auth_statement=$conn->prepare("SELECT password,public_key,private_key FROM user_info_table WHERE user_name=:user_name");
						$login_auth_statement->bindParam(':user_name',$user_name);
				


						$login_auth_statement->execute();
						$login_auth_statement->setFetchMode(PDO::FETCH_ASSOC);
						
						$login_auth_result=array();
						$login_keys=array();

						 while ($login_auth_response = $login_auth_statement->fetch()) {
							        $response_password= $login_auth_response['password'];
							        $login_keys['public_key']=$login_auth_response['public_key'];
					  				$login_keys['private_key']=$login_auth_response['public_key'];
						 }
						
						
						if($password==$response_password){
							
							
							$login_auth_result['keys']=$login_keys;
							$login_auth_result['success']=1;
							$login_auth_result['message']="authenticated";
							echo json_encode($login_auth_result);
						}else{
							$login_keys['public_key']="NIL";
							$login_keys['private_key']="NIL";
							$login_auth_result['keys']=$login_keys;
							$login_auth_result['success']=0;
							$login_auth_result['message']="not authenticated";
							echo json_encode($login_auth_result);
						}
						} else if($request_code==3){
							$user_id=$_POST['user_id'];
							$recorded_timestamp=$_POST['recorded_timestamp'];
							$value=$_POST['value'];
							$type=$_POST['type'];
							$in_sync=$_POST['in_sync'];
							$sync_timestamp=$_POST['sync_timestamp'];

							$sync_statement=$conn->prepare("INSERT INTO `user_vital_signs_table`(`unique_reading_id`,`user_id`,`recorded_timestamp`,`value`,`type`,`in_sync`,`sync_timestamp`) VALUES (DEFAULT,:user_id,:recorded_timestamp,:value,:type,:in_sync,:sync_timestamp)");
							$sync_statement->bindParam(':user_id',$user_id);
							$sync_statement->bindParam(':recorded_timestamp',$recorded_timestamp);
							$sync_statement->bindParam(':value',$value);
							$sync_statement->bindParam(':type',$type);
							$sync_statement->bindParam(':in_sync',$in_sync);
							$sync_statement->bindParam(':sync_timestamp',$sync_timestamp);

							$sync_statement->execute();

							$sign_up_result=array();
							$sign_up_result['success']=1;
							$sign_up_result['message']='readings inserted';
							echo json_encode($sign_up_result);

						}else if($request_code==4){
							$monitor_user_id=$_POST['monitor_user_id'];
							$target_user_id=$_POST['target_user_id'];

							$request_key_statement=$conn->prepare("SELECT public_key,private_key FROM user_info_table WHERE user_name=:target_user_id");
							$request_key_statement->bindParam(':target_user_id',$target_user_id);

							$request_key_statement->execute();
							$request_key_statement->setFetchMode(PDO::FETCH_ASSOC);

							$monitor_request_result=array();

							$target_keys=array();

							while ($request_key_response =$request_key_statement->fetch()) {
									$target_keys['public_key']=$request_key_response['public_key'];
									$target_keys['private_key']=$request_key_response['private_key'];
							}

							$request_vitals_statement=$conn->prepare("SELECT user_id,recorded_timestamp,value,type,in_sync,sync_timestamp FROM user_vital_signs_table WHERE user_id=:target_user_id");
							$request_vitals_statement->bindParam(':target_user_id',$target_user_id);

							$request_vitals_statement->execute();
							$request_vitals_statement->setFetchMode(PDO::FETCH_ASSOC);

							$request_vitals_result=array();
							$request_vitals_result_array=array();

							$i=0;
							while ($request_vitals_response=$request_vitals_statement->fetch()) {
									$request_vitals_result['user_id']=$request_vitals_response['user_id'];
									$request_vitals_result['recorded_timestamp']=$request_vitals_response['recorded_timestamp'];
									$request_vitals_result['value']=$request_vitals_response['value'];
									$request_vitals_result['type']=$request_vitals_response['type'];
									$request_vitals_result['in_sync']=$request_vitals_response['in_sync'];
									$request_vitals_result['sync_timestamp']=$request_vitals_response['sync_timestamp'];
									$request_vitals_result_array[$i]=$request_vitals_result;
									$i++;
							}

							$monitor_request_result['keys']=$target_keys;
							$monitor_request_result['request_vitals_result_array']=$request_vitals_result_array;

							echo json_encode($monitor_request_result);

						}

						else{
						$invalid_code_result=array();
						$invalid_code_result['success']=0;
						$invalid_code_result['message']="invalid request code";
						echo json_encode($invalid_code_result);
					}
				}
	}catch(PDOException $e){
		$general_error_result=array();
		$general_error_result['success']=0;
		$general_error_result['message']=$e->getMessage();
		echo json_encode($general_error_result);
 	}

	$conn = null;

?>
