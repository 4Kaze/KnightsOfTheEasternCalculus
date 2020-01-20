import lambda.applicant.AssignApplicant;
import lambda.applicant.UploadPhoto;
import lambda.applicant.applicant.Applicant;
import lambda.applicant.applicant.Experience;
import lambda.applicant.applicant.Univerity;
import lambda.model.CloseQuestion;
import lambda.model.OpenQuestion;
import lambda.model.SolvableOpenQuestion;
import lambda.model.TestInstance;
import lambda.test.AddTestInstance;
import lambda.test.GetUncheckedTestInstances;
import lambda.util.Utils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LambdaTests {

    @Test
    public void capitalize() {
        OpenQuestion o = new OpenQuestion();
        o.setCorrectAnswer("asd");
        o.setMaxScore(10);
        o.setQuestion("WHAT?");
        SolvableOpenQuestion so = new SolvableOpenQuestion(o);
        List<SolvableOpenQuestion> list = new ArrayList<>();
        list.add(so);
        TestInstance testInstance = new TestInstance();
        testInstance.setOpenQuestions(list);

        testInstance.getOpenQuestions().get(0).setCorrectAnswer(Utils.capitalize(testInstance.getOpenQuestions().get(0).getCorrectAnswer()));
        Assert.assertEquals("Asd", testInstance.getOpenQuestions().get(0).getCorrectAnswer());
    }

    @Test
    public void dontCapitalize() {
        CloseQuestion c = new CloseQuestion();
        c.setAnswerScore(10);
        c.setQuestion("What's the deal with fish?");
        ArrayList<String> list = new ArrayList<>();
        list.add("123");
        list.add("adfg");
        c.setCorrectAnswers(list);


        Assert.assertEquals(c.getCorrectAnswers().get(0), "123");
    }

    @Test
    public void applicantExp() {
        Applicant a = new Applicant();
        ArrayList<Experience> e = new ArrayList<>();
        e.add(new Experience());
        a.setExperiences(e);

        Assert.assertNotEquals(a.getExperiences(), null);
    }

    @Test
    public void applicantUni() {
        Applicant a = new Applicant();
        ArrayList<Univerity> u = new ArrayList<>();
        u.add(new Univerity());
        a.setUniversities(u);

        AddTestInstance adt = new AddTestInstance();
        GetUncheckedTestInstances guti = new GetUncheckedTestInstances();
        AssignApplicant aa = new AssignApplicant();
        UploadPhoto up = new UploadPhoto();

        Assert.assertNotEquals(a.getUniversities(), null);
    }

}
