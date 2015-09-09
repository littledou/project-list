package mobile.AUDetection;

public class FaceAnalyseResult {

	float [] m_landmarks;
	float [] m_pose;
	float [] m_emotionScore;

	public void GetFaceRes( float [] _landmarks, float [] _pose, float [] _emotionScore )
	{
		this.m_landmarks = _landmarks;
		this.m_pose = _pose;
		this.m_emotionScore = _emotionScore;
	}

	@Override
	public String toString() {
		return "landmarks = " +m_landmarks.toString()+"/n"
	+"m_pose = " +m_pose.toString()+"/n"
				+"m_emotionScore = " +m_emotionScore.toString()+"/n";
	}

}
