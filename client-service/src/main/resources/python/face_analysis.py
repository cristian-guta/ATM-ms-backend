import sys

sys.path.append('/Users/cristianguta/Desktop/ATM-BACKEND/review-service/venv/lib/python3.9/site-packages')
sys.path.append(
    '/Users/cristianguta/Desktop/ATM-BACKEND/review-service/venv/lib/python3.9/site-packages/azure/cognitiveservices/vision/face')
from azure.cognitiveservices.vision.face import FaceClient
from msrest.authentication import CognitiveServicesCredentials
import boto3 as boto3

# connect to amazon s3

s3 = boto3.resource(
    service_name='s3',
    region_name='eu-central-1',
    aws_access_key_id='',
    aws_secret_access_key=''
)


# face and emotion recognition

def get_face_client():
    """Create an authenticated FaceClient."""
    SUBSCRIPTION_KEY = ''
    ENDPOINT = ''
    credential = CognitiveServicesCredentials(SUBSCRIPTION_KEY)
    return FaceClient(ENDPOINT, credential)


def main():
    face_client = get_face_client()
    url = sys.argv[1]
    attributes = ["emotion", "glasses", "smile"]
    include_id = True
    include_landmarks = True
    detected_faces = face_client.face.detect_with_url(url, include_id, include_landmarks, attributes, raw=True)

    for atr in detected_faces.response.json():
        faceAttributes = atr['faceAttributes']
        emotion = faceAttributes['emotion']

        anger = emotion['anger']
        contempt = emotion['contempt']
        disgust = emotion['disgust']
        fear = emotion['fear']
        happiness = emotion['happiness']
        neutral = emotion['neutral']
        sadness = emotion['sadness']
        surprise = emotion['surprise']
        emotions_dict = {"anger": anger, 'contempt': contempt, 'disgust': disgust, 'fear': fear, 'happiness': happiness,
                         'neutral': neutral,
                         'sadness': sadness, 'surprise': surprise}
        emotions_dict = dict(sorted(emotions_dict.items(), reverse=True, key=lambda item: item[1]))
        first_element = list(emotions_dict.keys())[0]
        print(first_element)


if __name__ == '__main__':
    main()
