package com.exercises.base.exercise;

// import com.exercises.BallExercises.dribbling.DribblingExercise;
// import com.exercises.BallPersonExercises.ballPushup.BallPushUpExercise;
// import com.exercises.BallPersonExercises.inOut.InOutExercise;
// import com.exercises.BallPersonExercises.iniesta.BallBounces;
// import com.exercises.BallPersonExercises.insidePushPull.InsidePushPullExercise;
// import com.exercises.BallPersonExercises.juggling.JugglingExercise;
// import com.exercises.BallPersonExercises.pushPullLaces.PushPullLacesExercise;
// import com.exercises.BallPersonExercises.rollstop.RollStopsExercise;
// import com.exercises.BallPersonExercises.scissors.ScissorsExercise;
// import com.exercises.BallPersonExercises.soleRolls.SoleRollsExercise;
// import com.exercises.BallPersonExercises.stepOver.StepOverExercise;
// import com.exercises.BallPersonExercises.triangles.TrianglesExercise;
// import com.exercises.BallPersonExercises.vcuts.VcutsExercise;
import com.exercises.PersonExercises.bridge.BridgeExercise;
// import com.exercises.PersonExercises.bulgarianSplitSquat.BulgarianSplitSquat;
import com.exercises.PersonExercises.burpees.BurpeesExercise;
import com.exercises.PersonExercises.deadlift.DeadliftExercise;
// import com.exercises.PersonExercises.frameGame.FrameGameExercise;
import com.exercises.PersonExercises.growingGrass.GrowingGrassExercise;
import com.exercises.PersonExercises.highknee.HighKneeExercise;
// import com.exercises.PersonExercises.hurdleJump.HurdleJumpExercise;
import com.exercises.PersonExercises.jumpingSquat.JumpingSquatExercise;
import com.exercises.PersonExercises.jumpingjack.JumpingJackExercise;
import com.exercises.PersonExercises.kneePushup.KneePushupExercise;
import com.exercises.PersonExercises.kneesToChest.KneesToChestExercise;
import com.exercises.PersonExercises.ladderRun.LadderRunExercise;
import com.exercises.PersonExercises.legRaise.LegRaiseExercise;
import com.exercises.PersonExercises.lunges.LungesExercise;
import com.exercises.PersonExercises.orderGames.OrderGamesExercise;
import com.exercises.PersonExercises.personPose.PersonPoseExercise;
import com.exercises.PersonExercises.plank.PlankExercise;
import com.exercises.PersonExercises.plankCommandos.PlankCommandosExercise;
import com.exercises.PersonExercises.pushUp.PushUpExercise;
import com.exercises.PersonExercises.sideLegRaise.SideLegRaiseExercise;
import com.exercises.PersonExercises.sidePlank.SidePlankExercise;
import com.exercises.PersonExercises.sidePlankDips.SidePlankDipsExercise;
import com.exercises.PersonExercises.sideToSideHop.SideToSideHopExercise;
import com.exercises.PersonExercises.singleLegDeadlift.SingleLegDeadliftExercise;
import com.exercises.PersonExercises.sitUps.SitUpsExercise;
// import com.exercises.PersonExercises.skaterJump.SkaterJump;
import com.exercises.PersonExercises.squats.SquatsExercise;
import com.exercises.PersonExercises.touchPoint.TouchPointExercise;
import com.exercises.PersonExercises.tricepDips.TricepDipsExercise;
import com.exercises.PersonExercises.wallSit.WallSit;
// import com.exercises.nonAI.NonAIBallPersonExercise;
// import com.exercises.nonAI.NonAIPersonExercise;

import static com.exercises.base.exercise.ExerciseActivity.eActivity;

public class ExerciseHolder {

    protected static AbstractExercise mapExercise(ExerciseSettings exerciseSettings) {
        switch (eActivity.exerciseIndex) {
            case 0:
                return new HighKneeExercise(exerciseSettings);
            // case 1:
            //     return new JugglingExercise(exerciseSettings);
            case 2:
                return new JumpingJackExercise(exerciseSettings);
            case 3:
                return new JumpingSquatExercise(exerciseSettings);
            // case 4:
            //     return new VcutsExercise(exerciseSettings);
            case 5:
                return new SquatsExercise(exerciseSettings);
            case 6:
                return new DeadliftExercise(exerciseSettings);
            // case 7:
            //     return new BallBounces(exerciseSettings);
            case 8:
                return new WallSit(exerciseSettings);
            case 9:
                return new SitUpsExercise(exerciseSettings);
            case 10:
                return new PushUpExercise(exerciseSettings);
            // case 11:
            //     return new FrameGameExercise(exerciseSettings);
            case 12:
                return new KneePushupExercise(exerciseSettings);
            // case 13:
            //     return new DribblingExercise(exerciseSettings);
            case 14:
                return new LadderRunExercise(exerciseSettings);
            case 15:
                return new PlankExercise(exerciseSettings);
            case 16:
                return new SidePlankExercise(exerciseSettings);
            case 17:
                return new LegRaiseExercise(exerciseSettings);
            // case 18:
            //     return new GrowingGrassExercise(exerciseSettings);
            // case 19:
            //     return new SideLegRaiseExercise(exerciseSettings);
            // case 20:
            //     return new HurdleJumpExercise(exerciseSettings);
            // case 21:
            //     return new SideToSideHopExercise(exerciseSettings);
            // case 22:
            //     return new NonAIPersonExercise(exerciseSettings);
            // case 23:
            //     return new NonAIBallPersonExercise(exerciseSettings);
            // case 24:
            //     return new TouchPointExercise(exerciseSettings);
            // case 25:
            //     return new OrderGamesExercise(exerciseSettings);
            // case 26:
            //     return new PersonPoseExercise(exerciseSettings);
            // case 27:
            //     return new RollStopsExercise(exerciseSettings);
            case 28:
                return new BurpeesExercise(exerciseSettings);
            // case 29:
            //     return new SidePlankDipsExercise(exerciseSettings);
            // case 30:
            //     return new SkaterJump(exerciseSettings);
            // case 31:
            //     return new BallPushUpExercise(exerciseSettings);
            // case 32:
            //     return new ScissorsExercise(exerciseSettings);
            // case 33:
            //     return new StepOverExercise(exerciseSettings);
            // case 34:
            //     return new TrianglesExercise(exerciseSettings);
            // case 35:
            //     return new SoleRollsExercise(exerciseSettings);
            // case 36:
            //     return new KneesToChestExercise(exerciseSettings);
            // case 43:
            //     return new PlankCommandosExercise(exerciseSettings);
            // case 42:
            //     return new BulgarianSplitSquat(exerciseSettings);
            // case 45:
            //     return new BridgeExercise(exerciseSettings);
            // case 47:
            //     return new LungesExercise(exerciseSettings);
            // case 48:
            //     return new InsidePushPullExercise(exerciseSettings);
            // case 49:
            //     return new InOutExercise(exerciseSettings);
            // case 50:
            //     return new PushPullLacesExercise(exerciseSettings);
            // case 53:
            //     return new SingleLegDeadliftExercise(exerciseSettings);
            case 55:
                return new TricepDipsExercise(exerciseSettings);


            default:
                throw new IllegalStateException("Unexpected value: " + eActivity.exerciseIndex);
        }
    }
}