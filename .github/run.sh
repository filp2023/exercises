export PROJECT_NAME="exercises01"
export SCALAFIX=""

if [ "$GITHUB_REF" == "exercises02" ]; then
  export PROJECT_NAME="exercises02"
elif [ "$GITHUB_REF" == "exercises03" ]; then
  export PROJECT_NAME="exercises03"
  export SCALAFIX="scalafix"
elif [ "$GITHUB_REF" == "exercises04" ]; then
  export PROJECT_NAME="exercises04"
  export SCALAFIX="scalafix"
elif [ "$GITHUB_REF" == "exercises05" ]; then
  export PROJECT_NAME="exercises05"
  export SCALAFIX="scalafix"
elif [ "$GITHUB_REF" == "exercises06" ]; then
  export PROJECT_NAME="exercises06"
  export SCALAFIX="scalafix"
elif [ "$GITHUB_REF" == "exercises07" ]; then
  export PROJECT_NAME="exercises07"
  export SCALAFIX="scalafix"
elif [ "$GITHUB_REF" == "exercises08" ]; then
  export PROJECT_NAME="exercises08"
  export SCALAFIX="scalafix"
elif [ "$GITHUB_REF" == "exercises09" ]; then
  export PROJECT_NAME="exercises09"
  export SCALAFIX="scalafix"
elif [ "$GITHUB_REF" == "exercises10" ]; then
  export PROJECT_NAME="exercises10"
  export SCALAFIX="scalafix"
elif [ "$GITHUB_REF" == "exercises11" ]; then
  export PROJECT_NAME="exercises11"
  export SCALAFIX="scalafix"
elif [ "$GITHUB_REF" == "exercises12" ]; then
  export PROJECT_NAME="exercises12"
  export SCALAFIX="scalafix"
fi
echo "Running build for $PROJECT_NAME"
sbt "project $PROJECT_NAME" clean scalafmtCheck test:scalafmtCheck compile $SCALAFIX test