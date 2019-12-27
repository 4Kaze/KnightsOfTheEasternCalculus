import subprocess
import json
import time

print("Deleting dynamodb table...")
subprocess.call("aws dynamodb delete-table --table-name Applicant", shell=True)
subprocess.call("aws dynamodb delete-table --table-name Tests", shell=True)

print("Deleting lambdas...")
#delete get-applicant lambda
subprocess.call("aws lambda delete-function  --function-name get-applicant", shell=True)

#delete get-applicants lambda
subprocess.call("aws lambda delete-function --function-name get-applicants", shell=True)

#delete add-applicant lambda
subprocess.call("aws lambda delete-function --function-name add-applicant", shell=True)

#delete add-test lambda
subprocess.call("aws lambda delete-function --function-name add-test", shell=True)
#delete delete-test lambda
subprocess.call("aws lambda delete-function --function-name delete-test", shell=True)
#delete update-test lambda
subprocess.call("aws lambda delete-function --function-name update-test", shell=True)
#delete get-all-tests lambda
subprocess.call("aws lambda delete-function --function-name get-all-tests", shell=True)

print("Deleting gateways...")

gatewaysID = json.loads(subprocess.check_output("aws apigateway get-rest-apis", shell=True).decode("utf-8"))["items"]

for gateway in gatewaysID:
    print("\tDeleting {0} with id {1}".format(gateway["name"], gateway["id"]))
    subprocess.call("aws apigateway delete-rest-api --rest-api-id {}".format(gateway["id"]), shell=True)
    time.sleep(5)

print("Clearing S3 bucket...")
subprocess.call("aws s3 rm s3://applicant-photos --recursive", shell=True)

print("Deleting S3 bucket...")
subprocess.call("aws s3api delete-bucket --bucket applicant-photos", shell=True)

print("Deleting Cognito User Pools")
pools = json.loads(subprocess.check_output("aws cognito-idp list-user-pools --max-results 20", shell=True).decode("utf-8"))["UserPools"]
for pool in pools:
    if pool["Name"] == "kotec":
        pool_id = pool["Id"]
        break
subprocess.call("aws cognito-idp delete-user-pool --user-pool-id '{}'".format(pool_id), shell=True)

print("Deleting Cognito Identity Pools")
id_pools = json.loads(subprocess.check_output("aws cognito-identity list-identity-pools --max-results 20", shell=True).decode('utf-8'))["IdentityPools"]
for pool in id_pools:
    if pool["IdentityPoolName"] == "kotec_id":
        id_pool_id = pool["IdentityPoolId"]
        break
subprocess.call("aws cognito-identity delete-identity-pool --identity-pool-id {}".format(id_pool_id), shell=True)

print("Script finished.")
